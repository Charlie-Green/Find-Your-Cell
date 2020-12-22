package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.NavigationUtil
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.ui.common.interrupt.EditInterruptState
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.root.vm.PrisonerRootViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.prisoner_activity.*


/** This [Activity] combines screens that set up
  * [Prisoner]'s schedule and profile. **/
@AndroidEntryPoint
class PrisonerActivity: AppCompatActivity(R.layout.prisoner_activity) {

    private lateinit var vm: PrisonerRootViewModel
    private lateinit var navMan: PrisonerNavigationManager
    private var thereAreUnsavedChanges = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.also { AndroidUtil.defaultHideKeyboard(it) }

        initFields()
        navMan.setup( vm.prisonerLD.value != null )

        vm.savePrisonerResultLD.observe(this, Observer { result ->
            if(result is SavePrisonerResult.Success) {
                notifySavePrisonerSuccess()
                vm.notifySaveResultConsumed()
            }
        })

        vm.updateScheduleResultLD.observe(this, Observer { result ->
            if(result != null) {
                notifyUpdateScheduleSuccess()
                vm.notifyUpdateScheduleResultConsumed()
            }
        })
        
        vm.cellUpdateLD.observe(this, Observer { update ->
            if(update is CellUpdate.DeleteFailed) {
                notifyDeleteCellFailed()
                vm.notifyCellUpdateConsumed()
            }
        })

        vm.editInterruptStateLD.observe(this, Observer { state ->
            when(state) {
                is EditInterruptState.Confirmed -> interruptAndNavigate(state)
                is EditInterruptState.Asking    -> warnEditInterrupt()
            }
        })

        vm.unsavedChangesLD.observe(this, Observer { changes ->
            thereAreUnsavedChanges = changes
        })
    }

    override fun onBackPressed() {
        if(interruptingEdit) {
            val currentDest = navController.currentDestination?.id ?: 0
            vm.notifyEditInterrupted(currentDest, 0)
        } else {
            super.onBackPressed()
        }
    }


    private val navController: NavController
        get() = findNavController(R.id.navHost)

    private val interruptingEdit: Boolean
        get() {
            if(!thereAreUnsavedChanges) {
                return false
            }

            val destId = navController.currentDestination?.id ?: 0
            return (destId == R.id.fragmProfile || destId == R.id.fragmSchedule)
        }


    private fun initFields() {
        vm = PrisonerRootViewModel.get(applicationContext, this)
        navMan = PrisonerNavigationManager(vm, toolbar, navDrawer, navController)
    }

    private fun notifySavePrisonerSuccess()
        = notifySuccess(R.string.save_prisoner_success_msg)

    private fun notifyUpdateScheduleSuccess()
        = notifySuccess(R.string.update_schedule_success_msg)

    private fun notifyDeleteCellFailed()
        = notifyError(R.string.delete_cell_failed_msg)


    private fun notifySuccess(messageRes: Int) {
        Snackbar.make(cdltRoot, messageRes, 2000).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }

    private fun warnEditInterrupt() {
        NavigationUtil.navigateIfNotYet(
            navController,
            R.id.dialogEditInterrupt
        ) { null }
    }

    private fun interruptAndNavigate(state: EditInterruptState.Confirmed) {
        navMan.doOnce(state.source) {
            cdltRoot.post {  // In order to avoid the FragmentManager-in-Transaction failure.
                navigate(state.dest)
                vm.notifyInterruptConfirmationConsumed()
            }
        }
    }

    private fun navigate(destRes: Int) {
        if(destRes == 0) {
            super.onBackPressed()
            return
        }
        navController.navigate(destRes)
    }


    private fun notifyError(messageRes: Int) {
        Snackbar.make(cdltRoot, messageRes, 3000).apply {
            setTextColor(Color.RED)
            show()
        }
    }
}