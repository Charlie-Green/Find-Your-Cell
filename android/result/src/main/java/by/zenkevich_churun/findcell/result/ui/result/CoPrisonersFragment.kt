package by.zenkevich_churun.findcell.result.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonersFragmBinding


class CoPrisonersFragment: SviazenFragment<CoprisonersFragmBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSwipeRefresh()

    }


    private fun setupSwipeRefresh() {
        val primaryColor = AndroidUtil.themeColor(requireContext(), R.attr.colorPrimary)
        val accentColor  = AndroidUtil.themeColor(requireContext(), R.attr.colorAccent)
        vb.srl.setColorSchemeColors(primaryColor, accentColor)

        vb.srl.setOnRefreshListener {
            Log.v("CharlieDebug", "Refreshing! Thread is main: ${AndroidUtil.isThreadMain}")
        }
    }
}