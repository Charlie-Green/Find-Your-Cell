package by.zenkevich_churun.findcell.result.ui.cps.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.TabsAndPagerListener
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonersFragmBinding
import by.zenkevich_churun.findcell.result.ui.cps.model.RefreshState
import by.zenkevich_churun.findcell.result.ui.cps.vm.CoPrisonersViewModel
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestState


class CoPrisonersFragment: SviazenFragment<CoprisonersFragmBinding>() {

    private lateinit var vm: CoPrisonersViewModel
    private var isSyncing = false
    private var isSendingConnectRequest = false


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        setupTabs()
        setupRefresh()
        setupReturnToProfile()
        vm.onViewCreated()

        vm.showSyncLD.observe(viewLifecycleOwner) { show ->
            isSyncing = show
            updateProgressBarVisibility()
        }
        vm.refreshStateLD.observe(viewLifecycleOwner) { state ->
            renderRefreshState(state)
        }
        vm.sendConnectRequestStateLD.observe(viewLifecycleOwner) { state ->
            renderConnectRequestState(state)
        }
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

    private fun setupRefresh() {
        val primaryColor = AndroidUtil.themeColor(requireContext(), R.attr.colorPrimary)
        val accentColor  = AndroidUtil.themeColor(requireContext(), R.attr.colorAccent)
        vb.refreshLayout.setColorSchemeColors(primaryColor, accentColor)

        vb.refreshLayout.setOnRefreshListener {
            vm.refresh()
        }
    }

    private fun setupReturnToProfile() {
        val clickListener = View.OnClickListener {
            // TODO
        }

        vb.txtvProfile.setOnClickListener(clickListener)
        vb.imgvProfile.setOnClickListener(clickListener)
    }



    private fun renderRefreshState(state: RefreshState) {
        vb.refreshLayout.isRefreshing = (state == RefreshState.REFRESHING)

        when(state) {
            RefreshState.NO_INTERNET -> {
                showErrorDialog(
                    R.string.refresh_failed_title,
                    R.string.refresh_needs_internet_msg
                ) { vm.onRefreshStateNotified() }
            }

            RefreshState.ERROR -> {
                showErrorDialog(
                    R.string.refresh_failed_title,
                    R.string.network_error_msg
                ) { vm.onRefreshStateNotified() }
            }
        }
    }

    private fun renderConnectRequestState(state: ConnectRequestState) {
        isSendingConnectRequest = (state is ConnectRequestState.Sending)
        updateProgressBarVisibility()

        if(state is ConnectRequestState.NetworkError && !state.notified) {
            showErrorDialog(
                R.string.send_connect_request_failed_title,
                R.string.network_error_msg
            ) { state.notified = true }
        }
    }


    private fun updateProgressBarVisibility() {
        vb.prBar.isVisible = isSyncing || isSendingConnectRequest
    }
}