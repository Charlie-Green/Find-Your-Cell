package by.zenkevich_churun.findcell.result.ui.request.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.ConnectRequestsFragmBinding
import by.zenkevich_churun.findcell.result.ui.cps.model.RefreshState
import by.zenkevich_churun.findcell.result.ui.request.vm.ConnectRequestsViewModel


class ConnectRequestsFragment: SviazenFragment<ConnectRequestsFragmBinding>() {

    private lateinit var vm: ConnectRequestsViewModel


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = ConnectRequestsFragmBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        obtainViewModel()
        setupRefresh()
        vm.onViewCreated()
    }


    private fun obtainViewModel() {
        val page = childFragmentManager.fragments.find { fragm ->
            fragm is ConnectRequestsPage
        } as ConnectRequestsPage
        vm = page.viewModel
    }


    private fun setupRefresh() {
        AndroidUtil.setRefreshThemeColors(
            vb.refreshLayout, R.attr.colorPrimary, R.attr.colorAccent )

        vb.refreshLayout.setOnRefreshListener {
            vm.refresh()
        }
        vm.refreshStateLD.observe(viewLifecycleOwner, this::displayRefreshState)
    }

    private fun displayRefreshState(state: RefreshState) {
        vb.refreshLayout.isRefreshing = (state is RefreshState.InProgress)
        when(state) {
            is RefreshState.NoInternet   -> notifyNoInternet(state)
            is RefreshState.NetworkError -> notifyNetworkError(state)
        }
    }

    private fun notifyNoInternet(state: RefreshState.NoInternet) {
        if(state.notified) {
            return
        }

        showErrorDialog(
            R.string.refresh_failed_title,
            R.string.refresh_requests_needs_internet_msg
        ) { state.notified = true }
    }

    private fun notifyNetworkError(state: RefreshState.NetworkError) {
        if(state.notified) {
            return
        }

        showErrorDialog(
            R.string.refresh_failed_title,
            R.string.network_error_msg
        ) { state.notified = true }
    }
}