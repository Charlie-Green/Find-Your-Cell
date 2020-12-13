package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
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
    private var thereAreUnsavedChanges = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = PrisonerRootViewModel.get(applicationContext, this)
        setupNavigation()
        setupNavigationDrawer()

        vm.savePrisonerResultLD.observe(this, Observer { result ->
            if(result == SavePrisonerResult.SUCCESS) {
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
            if(state == EditInterruptState.ASKING) {
                warnEditInterrupt()
            }
        })

        vm.unsavedChangesLD.observe(this, Observer { changes ->
            thereAreUnsavedChanges = changes
        })
    }

    override fun onBackPressed() {
        if(interruptingEdit) {
            vm.notifyEditInterrupted()
        } else {
            super.onBackPressed()
        }
    }


    private val interruptingEdit: Boolean
        get() {
            if(!thereAreUnsavedChanges) {
                return false
            }

            val navController = findNavController(R.id.navHost)
            val destId = navController.currentDestination?.id ?: 0
            return (destId == R.id.fragmProfile || destId == R.id.fragmSchedule)
        }


    private fun setupNavigation() {
        val authorized = (vm.prisonerLD.value != null)

        val navController = findNavController(R.id.navHost)
        val prisonerGraph = navController.navInflater.inflate(R.navigation.prisoner)
        prisonerGraph.startDestination =
            if(authorized) R.id.fragmProfile
            else R.id.fragmAuth

        navController.graph = prisonerGraph
    }

    private fun setupNavigationDrawer() {
        PrisonerNavigationDrawerManager(
            navDrawer,
            findNavController(R.id.navHost)
        ).setup()
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

    private fun notifyError(messageRes: Int) {
        Snackbar.make(cdltRoot, messageRes, 3000).apply {
            setTextColor(Color.RED)
            show()
        }
    }

    private fun warnEditInterrupt() {
        NavigationUtil.navigateIfNotYet(
            findNavController(R.id.navHost),
            R.id.dialogEditInterrupt
        ) { null }
    }
}