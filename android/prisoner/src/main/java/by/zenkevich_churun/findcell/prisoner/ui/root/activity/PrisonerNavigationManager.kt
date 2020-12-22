package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.NavigationUtil
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.root.vm.PrisonerRootViewModel
import com.google.android.material.navigation.NavigationView


internal class PrisonerNavigationManager(
    private val vm: PrisonerRootViewModel,
    private val toolbar: Toolbar,
    private val drawer: NavigationView,
    private val controller: NavController ) {

    private var actionFrom = 0
    private var action: (() -> Unit)? = null


    fun setup() {
        inflateGraph()

        drawer.setNavigationItemSelectedListener { item ->
            navigate(item.itemId).also {
                (drawer.parent as DrawerLayout).closeDrawers()
            }
        }

        controller.addOnDestinationChangedListener { _, dest, _ ->
            select(dest.id)
            setTitle(dest.label)
            setDrawerEnabled()

            vm.lastDestination = dest.id
            optionallyInvokeAction()
        }
    }

    /** Authomatically navigate back when destination reaches the given one. **/
    fun doOnce(destId: Int, what: () -> Unit) {
        actionFrom = destId
        action = what
        optionallyInvokeAction()
    }


    private val appName: String
        get() = AndroidUtil.stringByResourceName(toolbar.context, "app_name") ?: ""

    private fun inflateGraph() {
        val startDest = when {
            vm.prisonerLD.value == null            -> R.id.fragmAuth
            vm.lastDestination == R.id.fragmArests -> R.id.fragmArests
            else                                   -> R.id.fragmProfile
        }

        val prisonerGraph = controller.navInflater.inflate(R.navigation.prisoner)
        prisonerGraph.startDestination = startDest
        controller.graph = prisonerGraph
    }

    private fun navigate(itemId: Int): Boolean {
        val currentDest = controller.currentDestination?.id ?: 0
        val unsavedChanges = vm.unsavedChangesLD.value ?: false

        when(itemId) {
            R.id.miProfile -> {
                NavigationUtil.navigateIfNotYet(
                    controller,
                    R.id.fragmProfile,
                    R.id.actSelectProfileMenu
                ) { null }
            }

            R.id.miArests  -> {
                if(unsavedChanges) {
                    vm.notifyEditInterrupted(currentDest, R.id.actSelectArestsMenu)
                    return false
                }

                NavigationUtil.navigateIfNotYet(
                    controller,
                    R.id.fragmArests,
                    R.id.actSelectArestsMenu
                ) { null }
            }

            R.id.miAuth -> {
                if(unsavedChanges) {
                    vm.notifyEditInterrupted(currentDest, R.id.actSelectAuthMenu)
                    return false
                }

                NavigationUtil.navigateIfNotYet(
                    controller,
                    R.id.fragmAuth,
                    R.id.actSelectAuthMenu
                ) { null }
                vm.signOut()
            }

            else -> {
                throw NotImplementedError("Unknown menu item $itemId")
            }
        }

        return true
    }

    private fun select(destId: Int) {
        val itemId = when(destId) {
            R.id.fragmProfile -> R.id.miProfile
            R.id.fragmArests  -> R.id.miArests
            R.id.fragmAuth    -> R.id.miAuth
            else -> return
        }

        if(drawer.checkedItem?.itemId != itemId) {
            drawer.setCheckedItem(itemId)
        }
    }

    private fun optionallyInvokeAction() {
        val act = action ?: return
        if(vm.lastDestination == actionFrom) {
            action = null
            act()
        }
    }

    private fun setDrawerEnabled() {
        val enabled = (controller.currentDestination?.id != R.id.fragmAuth)
        val lockMode =
            if(enabled) DrawerLayout.LOCK_MODE_UNLOCKED
            else DrawerLayout.LOCK_MODE_LOCKED_CLOSED

        (drawer.parent as DrawerLayout).setDrawerLockMode(lockMode)
    }

    private fun setTitle(title: CharSequence?) {
        toolbar.title = title ?: appName
    }
}