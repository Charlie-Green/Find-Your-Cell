package by.sviazen.result.ui.shared.cpcontainer.fragm

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


internal class CoPrisonersPagerAdapter(
    fragment: Fragment,
    private val containerDescriptor: CoPrisonersContainerDescriptor<*>
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int
        = containerDescriptor.tabCount

    override fun createFragment(position: Int): Fragment
        = containerDescriptor.createPage(position)
}