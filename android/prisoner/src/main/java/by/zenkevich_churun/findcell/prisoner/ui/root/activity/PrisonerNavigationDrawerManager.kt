package by.zenkevich_churun.findcell.prisoner.ui.root.activity

import android.util.Log
import com.google.android.material.navigation.NavigationView


internal class PrisonerNavigationDrawerManager(
    private val drawer: NavigationView ) {

    fun setup() {
        drawer.setNavigationItemSelectedListener { item ->
            Log.v("CharlieDebug", "Checked item id ${item.itemId}, checked = ${item.isChecked}")
            true  // Yes, display this item as the selected one.
        }
    }
}