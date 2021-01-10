package by.zenkevich_churun.findcell.result.ui.cps.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.TabsAndPagerListener
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonersFragmBinding
import by.zenkevich_churun.findcell.result.ui.cps.vm.CoPrisonersViewModel


class CoPrisonersFragment: SviazenFragment<CoprisonersFragmBinding>() {

    private lateinit var vm: CoPrisonersViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        setupTabs()
        setupSwipeRefresh()
        setupReturnToProfile()
        vm.onViewCreated()
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CoPrisonersViewModel.get(appContext, this)
    }

    private fun setupTabs() {
        addTab(0)
        addTab(1)
        TabsAndPagerListener(vb.tabs, vb.vPager).attach()
        vb.vPager.adapter = CoPrisonersPagerAdapter(requireActivity())

    }

    private fun addTab(position: Int) {
        val labelRes = when(position) {
            CoPrisonersPagerAdapter.POSITION_SUGGESTED -> R.string.suggested_coprisoners_tabtext
            CoPrisonersPagerAdapter.POSITION_CONNECTED -> R.string.connected_coprisoners_tabtext
            else -> throw IllegalArgumentException("Unexpected tab position $position")
        }

        val tab = vb.tabs.newTab()
        tab.setText(labelRes)
        vb.tabs.addTab(tab)
    }

    private fun setupSwipeRefresh() {
        val primaryColor = AndroidUtil.themeColor(requireContext(), R.attr.colorPrimary)
        val accentColor  = AndroidUtil.themeColor(requireContext(), R.attr.colorAccent)
        vb.refreshLayout.setColorSchemeColors(primaryColor, accentColor)

        vb.refreshLayout.setOnRefreshListener {
            // TODO
        }
    }

    private fun setupReturnToProfile() {
        val clickListener = View.OnClickListener {
            // TODO
        }

        vb.txtvProfile.setOnClickListener(clickListener)
        vb.imgvProfile.setOnClickListener(clickListener)
    }
}