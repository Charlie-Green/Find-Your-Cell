package by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.fragm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


internal class CoPrisonersPagerAdapter(
    activity: FragmentActivity,
    private val containerDescriptor: CoPrisonersContainerDescriptor<*>
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int
        = containerDescriptor.tabCount

    override fun createFragment(position: Int): Fragment
        = containerDescriptor.createPage(position)
}