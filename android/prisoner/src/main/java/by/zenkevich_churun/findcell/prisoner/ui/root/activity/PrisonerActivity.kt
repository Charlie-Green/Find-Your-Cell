package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import by.zenkevich_churun.findcell.core.ui.common.SviazenActivity
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.NavigationUtil
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.databinding.PrisonerActivityBinding
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.ui.common.interrupt.EditInterruptState
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState
import by.zenkevich_churun.findcell.prisoner.ui.root.vm.PrisonerRootViewModel
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleCrudState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


/** This [Activity] combines screens that set up
  * [Prisoner]'s schedule and profile. **/
@AndroidEntryPoint
class PrisonerActivity: SviazenActivity<PrisonerActivityBinding>() {

    private lateinit var vm: PrisonerRootViewModel
    private lateinit var navMan: PrisonerNavigationManager
    private var thereAreUnsavedChanges = false


    override fun inflateViewBinding()
        = PrisonerActivityBinding.inflate(layoutInflater)

    override fun customizeView(v: View) {
        window?.also { AndroidUtil.defaultHideKeyboard(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFields()
        navMan.setup()

        vm.savePrisonerResultLD.observe(this, { result ->
            if(result is SavePrisonerResult.Success) {
                notifySuccess(R.string.save_prisoner_success_msg)
                vm.notifySaveResultConsumed()
            }
        })

        vm.scheduleCrudStateLD.observe(this, { state ->
            applyScheduleCrudState(state)
        })

        vm.cellCrudStateLD.observe(this, { state ->
            applyCellCrudState(state)
        })

        vm.editInterruptStateLD.observe(this, { state ->
            when(state) {
                is EditInterruptState.Confirmed -> interruptAndNavigate(state)
                is EditInterruptState.Asking    -> warnEditInterrupt()
            }
        })

        vm.unsavedChangesLD.observe(this, { changes ->
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
        navMan = PrisonerNavigationManager(
            vm,
            vb.toolbar,
            vb.navDrawer,
            navController
        )
    }


    private fun applyScheduleCrudState(state: ScheduleCrudState) {
        when(state) {
            is ScheduleCrudState.Updated -> {
                if(!state.notified) {
                    state.notified = true
                    notifySuccess(R.string.update_schedule_success_msg)
                }
            }
        }
    }

    private fun applyCellCrudState(state: ScheduleCellsCrudState) {
        when(state) {
            is ScheduleCellsCrudState.DeleteFailed -> {
                if(!state.notified) {
                    state.notified = true
                    notifyError(R.string.delete_cell_failed_msg)
                }
            }

            is ScheduleCellsCrudState.Added -> {
                if(!state.notified) {
                    state.notified = true

                    val msg = getString(
                        R.string.add_cell_success_msg,
                        state.newCell.number,
                        state.newCell.jailName
                    )
                    notifySuccess(msg)
                }
            }

            is ScheduleCellsCrudState.Updated -> {
                if(!state.notified) {
                    state.notified = true
                    notifySuccess(R.string.update_cell_succeeded_msg)
                }
            }

            is ScheduleCellsCrudState.Deleted -> {
                if(!state.notified) {
                    state.notified = true
                    notifySuccess(R.string.delete_cell_success_msg)
                }
            }
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
            vb.cdltRoot.post {  // In order to avoid the FragmentManager-in-Transaction failure.
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


    private fun notifySuccess(message: String) {
        Snackbar.make(vb.cdltRoot, message, 2000).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }

    private fun notifySuccess(messageRes: Int)
        = notifySuccess( getString(messageRes) )

    private fun notifyError(messageRes: Int) {
        Snackbar.make(vb.cdltRoot, messageRes, 3000).apply {
            setTextColor(Color.RED)
            show()
        }
    }
}