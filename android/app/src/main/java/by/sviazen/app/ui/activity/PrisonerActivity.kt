package by.sviazen.app.ui.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import by.sviazen.app.R
import by.sviazen.app.databinding.PrisonerActivityBinding
import by.sviazen.core.ui.common.SviazenActivity
import by.sviazen.core.util.android.NavigationUtil
import by.sviazen.core.repo.profile.SavePrisonerResult
import by.sviazen.prisoner.ui.common.interrupt.EditInterruptState
import by.sviazen.prisoner.ui.common.sched.period.ScheduleCellsCrudState
import by.sviazen.app.ui.vm.PrisonerRootViewModel
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.core.util.android.RespectStatusBarInsetsListener
import by.sviazen.prisoner.ui.sched.model.ScheduleCrudState
import by.sviazen.result.ui.contact.model.GetCoPrisonerState
import by.sviazen.result.ui.shared.cppage.model.ChangeRelationRequestState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


/** This [Activity] combines screens that set up
  * [Prisoner]'s schedule and profile. **/
@AndroidEntryPoint
class PrisonerActivity: SviazenActivity<PrisonerActivityBinding>() {
    // ==============================================================================
    // Fields:

    private lateinit var vm: PrisonerRootViewModel
    private lateinit var navMan: PrisonerNavigationManager
    private var thereAreUnsavedChanges = false


    // ==============================================================================
    // Lifecycle:

    override fun inflateViewBinding()
        = PrisonerActivityBinding.inflate(layoutInflater)

    override fun customizeView(v: View) {
        window?.also { win ->
            AndroidUtil.defaultHideKeyboard(win)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFields()
        navMan.setup()
        setupToolbar()
        setupKeyboadAnimation()
        handleInsets()

        vm.savePrisonerResultLD.observe(this) { result ->
            if(result is SavePrisonerResult.Success) {
                notifySuccess(R.string.save_prisoner_success_msg)
                vm.notifySaveResultConsumed()
            }
        }

        vm.scheduleCrudStateLD.observe(this) { state ->
            applyScheduleCrudState(state)
        }

        vm.cellCrudStateLD.observe(this) { state ->
            applyCellCrudState(state)
        }

        vm.editInterruptStateLD.observe(this) { state ->
            when(state) {
                is EditInterruptState.Confirmed -> interruptAndNavigate(state)
                is EditInterruptState.Asking    -> warnEditInterrupt()
            }
        }

        vm.unsavedChangesLD.observe(this) { changes ->
            thereAreUnsavedChanges = changes
        }

        vm.coPrisonerStateLD.observe(this) { state ->
            applyCoPrisonerState(state)
        }
        
        vm.changeRelationStateLD.observe(this) { state ->
            applyChangeRelationState(state)
        }
    }

    override fun onBackPressed() {
        if(interruptingEdit) {
            val currentDest = navController.currentDestination?.id ?: 0
            vm.notifyEditInterrupted(currentDest, 0)
        } else {
            super.onBackPressed()
        }
    }

    override fun onActionModeStarted(mode: ActionMode) {
        super.onActionModeStarted(mode)
        vb.toolbar.visibility = View.GONE
    }

    override fun onActionModeFinished(mode: ActionMode) {
        super.onActionModeFinished(mode)
        vb.toolbar.visibility = View.VISIBLE
    }


    // ==============================================================================
    // Private (Initialization):

    private fun initFields() {
        vm = PrisonerRootViewModel.get(applicationContext, this)
        navMan = PrisonerNavigationManager(
            vm,
            vb.toolbar,
            vb.navDrawer,
            navController
        )
    }

    private fun setupKeyboadAnimation() {
        if(Build.VERSION.SDK_INT >= 30) {
            KeyboardAnimationForPrisonerCallback.setTo(window, vb.root)
        }
    }

    private fun setupToolbar() {
        vb.toolbar.setNavigationOnClickListener {
            val drawerLayout = vb.navDrawer.parent as DrawerLayout
            if(drawerLayout.isDrawerOpen(vb.navDrawer)) {
                drawerLayout.closeDrawer(vb.navDrawer)
            } else {
                drawerLayout.openDrawer(vb.navDrawer)
            }
        }
    }

    private fun handleInsets() {
        if(Build.VERSION.SDK_INT >= 30) {
            val insetsListener = RespectStatusBarInsetsListener(vb.toolbar)
            vb.toolbar.setOnApplyWindowInsetsListener(insetsListener)
        }
    }


    // ==============================================================================
    // Private (States):

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

    private fun applyCoPrisonerState(state: GetCoPrisonerState) {
        if(state !is GetCoPrisonerState.Error || state.containerConsumed) {
            return
        }
        state.containerConsumed = true

        when(state) {
            is GetCoPrisonerState.Error.Network -> {
                notifyError(R.string.network_error_title)
            }
            is GetCoPrisonerState.Error.NoInternet -> {
                notifyError(R.string.no_internet_title)
            }
            is GetCoPrisonerState.Error.NotConnected -> {
                val msg = getString(R.string.not_coprisoner_msg, state.prisonerName)
                notifyError(msg)
            }
        }
    }

    private fun applyChangeRelationState(state: ChangeRelationRequestState) {
        if(state !is ChangeRelationRequestState.Success || state.notified) {
            return
        }
        state.notified = true

        notifySuccess(state.message)
    }


    // ==============================================================================
    // Private (Edit Interript):

    private fun warnEditInterrupt() {
        NavigationUtil.navigateIfNotYet(
            navController,
            R.id.dialogEditInterrupt
        ) { null }
    }

    private fun interruptAndNavigate(state: EditInterruptState.Confirmed) {
        navMan.doOnce(state.source) {
            vb.cdltRoot.post {  // In order to avoid the FragmentManager-in-Transaction failure.
                if(state.dest == R.id.actSelectAuthMenu) {
                    vm.logOut()
                }

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


    // ==============================================================================
    // Private (Notifications):

    private fun notifySuccess(message: String) {
        Snackbar.make(vb.cdltRoot, message, 2000).apply {
            setTextColor(Color.WHITE)
            show()
        }
    }

    private fun notifySuccess(messageRes: Int)
        = notifySuccess( getString(messageRes) )

    private fun notifyError(message: String) {
        Snackbar.make(vb.cdltRoot, message, 3000).apply {
            setTextColor(Color.RED)
            show()
        }
    }

    private fun notifyError(messageRes: Int)
        = notifyError( getString(messageRes) )


    // ==============================================================================
    // Private (Help):

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
}