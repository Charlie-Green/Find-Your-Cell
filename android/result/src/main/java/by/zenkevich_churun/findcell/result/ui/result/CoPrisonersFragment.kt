package by.zenkevich_churun.findcell.result.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonersFragmBinding
import com.google.android.material.tabs.TabLayout


class CoPrisonersFragment: SviazenFragment<CoprisonersFragmBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupTabs()
        setupSwipeRefresh()
    }


    private fun setupTabs() {
        val tab1 = vb.tabs.newTab().apply { setText(R.string.potential_coprisoners_tabtext) }
        val tab2 = vb.tabs.newTab().apply { setText(R.string.added_coprisoners_tabtext) }
        vb.tabs.addTab(tab1)
        vb.tabs.addTab(tab2)
    }

    private fun setupSwipeRefresh() {
        val primaryColor = AndroidUtil.themeColor(requireContext(), R.attr.colorPrimary)
        val accentColor  = AndroidUtil.themeColor(requireContext(), R.attr.colorAccent)
        vb.refreshLayout.setColorSchemeColors(primaryColor, accentColor)

        vb.refreshLayout.setOnRefreshListener {
            Log.v("CharlieDebug", "Refreshing! Thread is main: ${AndroidUtil.isThreadMain}")
        }
    }
}