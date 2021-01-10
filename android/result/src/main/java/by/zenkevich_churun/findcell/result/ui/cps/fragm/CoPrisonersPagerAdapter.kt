package by.zenkevich_churun.findcell.result.ui.cps.fragm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.zenkevich_churun.findcell.result.ui.connect.fragm.ConnectedCoPrisonersPage
import by.zenkevich_churun.findcell.result.ui.suggest.fragm.SuggestedCoPrisonersPage


internal class CoPrisonersPagerAdapter(
    activity: FragmentActivity
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int
        = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            POSITION_SUGGESTED -> SuggestedCoPrisonersPage()
            POSITION_CONNECTED -> ConnectedCoPrisonersPage()
            else -> throw IllegalArgumentException("Page $position")
        }
    }


    companion object {
        const val POSITION_SUGGESTED = 0
        const val POSITION_CONNECTED = 1
    }
}