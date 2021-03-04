package by.sviazen.app.ui.activity

import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import by.sviazen.app.R
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.app.ui.vm.PrisonerRootViewModel
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
            select(dest.parent?.id ?: dest.id)
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
            vm.prisonerLD.value == null            -> R.id.graphAuth
            vm.lastDestination == R.id.fragmArests -> R.id.graphArests
            else                                   -> R.id.graphProfile
        }

        val prisonerGraph = controller.navInflater.inflate(R.navigation.root)
        prisonerGraph.startDestination = startDest
        controller.graph = prisonerGraph
    }

    private fun navigate(itemId: Int): Boolean {
        val currentDest = controller.currentDestination?.id ?: 0
        val unsavedChanges = vm.unsavedChangesLD.value ?: false
        val desiredNav = when(itemId) {
            R.id.miProfile  -> R.id.actSelectProfileMenu
            R.id.miArests   -> R.id.actSelectArestsMenu
            R.id.miCps      -> R.id.actSelectCpsMenu
            R.id.miRequests -> R.id.actSelectRequestsMenu
            R.id.miAuth     -> R.id.actSelectAuthMenu
            else -> throw NotImplementedError("Unknown menu item $itemId")
        }

        if(unsavedChanges && itemId != R.id.miProfile) {
            vm.notifyEditInterrupted(currentDest, desiredNav)
            return false
        }

        vm.navigateTo(currentDest, desiredNav)
        return true
    }

    private fun select(destId: Int) {
        val itemId = when(destId) {
            R.id.graphProfile     -> R.id.miProfile
            R.id.graphArests      -> R.id.miArests
            R.id.graphCpsGeneral  -> R.id.miCps
            R.id.graphCpsRequests -> R.id.miRequests
            R.id.graphAuth        -> R.id.miAuth
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