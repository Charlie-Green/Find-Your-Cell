package by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.fragm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.zenkevich_churun.findcell.result.ui.connect.fragm.ConnectedCoPrisonersPage
import by.zenkevich_churun.findcell.result.ui.suggest.fragm.SuggestedCoPrisonersPage


internal class CoPrisonersPagerAdapter(
    activity: FragmentActivity,
    private val containerDescriptor: CoPrisonersContainerDescriptor<*>
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int
        = containerDescriptor.tabCount

    override fun createFragment(position: Int): Fragment
        = containerDescriptor.createPage(position)

//        return when(position) {
//            POSITION_SUGGESTED -> SuggestedCoPrisonersPage()
//            POSITION_CONNECTED -> ConnectedCoPrisonersPage()
//            else -> throw IllegalArgumentException("Page $position")
//        }



//    companion object {
//        const val POSITION_SUGGESTED = 0
//        const val POSITION_CONNECTED = 1
//    }
}