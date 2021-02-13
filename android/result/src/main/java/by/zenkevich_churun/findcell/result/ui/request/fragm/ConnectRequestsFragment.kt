package by.zenkevich_churun.findcell.result.ui.request.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.result.databinding.ConnectRequestsFragmBinding
import by.zenkevich_churun.findcell.result.ui.cps.model.RefreshState


class ConnectRequestsFragment: SviazenFragment<ConnectRequestsFragmBinding>() {

    private lateinit var page: ConnectRequestsPage


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = ConnectRequestsFragmBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findPage()
        setupRefresh()
    }


    private fun findPage() {
        page = childFragmentManager.fragments.find { fragm ->
            fragm is ConnectRequestsPage
        } as ConnectRequestsPage
    }


    private fun setupRefresh() {
        vb.refreshLayout.setOnRefreshListener {
            page.viewModel.refresh()
        }

        page.viewModel.refreshStateLD
            .observe(viewLifecycleOwner, this::displayRefreshState)
    }

    private fun displayRefreshState(state: RefreshState) {
        vb.refreshLayout.isRefreshing = (state == RefreshState.REFRESHING)
        // TODO: Show error
    }
}