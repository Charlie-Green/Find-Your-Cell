package by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.fragm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import by.zenkevich_churun.findcell.core.ui.common.SviazenFragment
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.core.util.android.TabsAndPagerListener
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.databinding.CoprisonersContainerFragmBinding
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.model.RefreshState
import by.zenkevich_churun.findcell.result.ui.shared.cppage.model.ChangeRelationRequestState
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.vm.CoPrisonersContainerViewModel


/** Defines a screen which is a container for [CoPrisoner] pages.
  * The pages can be switched using tabs.
  * Swipe-to-refresh is also supported. **/
abstract class CoPrisonersContainerFragment<
    ViewModelType: CoPrisonersContainerViewModel
>: SviazenFragment<CoprisonersContainerFragmBinding>() {

    private lateinit var vm: ViewModelType
    private lateinit var containerDescriptor: CoPrisonersContainerDescriptor<ViewModelType>
    private var isSyncing = false
    private var isSendingConnectRequest = false


    override fun inflateViewBinding(
        inflater: LayoutInflater
    ) = CoprisonersContainerFragmBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
        setupTabs()
        setupRefresh()

        vm.onViewCreated()

        vm.showSyncLD.observe(viewLifecycleOwner) { show ->
            isSyncing = show
            updateProgressBarVisibility()
        }
        vm.refreshStateLD.observe(viewLifecycleOwner) { state ->
            renderRefreshState(state)
        }
        vm.changeRelationStateLD.observe(viewLifecycleOwner) { state ->
            renderChangeRelationState(state)
        }
    }


    private fun initFields() {
        containerDescriptor = provideContainerDescriptor()
        val appContext = requireContext().applicationContext
        vm = containerDescriptor.getViewModel(appContext)
    }

    private fun setupTabs() {
        for(j in 0 until containerDescriptor.tabCount) {
            addTab(j)
        }
        TabsAndPagerListener(vb.tabs, vb.vPager).attach()
        vb.vPager.adapter =
            CoPrisonersPagerAdapter(this, containerDescriptor)

    }

    private fun addTab(position: Int) {
        val tab = vb.tabs.newTab()
        tab.setText( containerDescriptor.tabLabelRes(position) )
        vb.tabs.addTab(tab)
    }

    private fun setupRefresh() {
        AndroidUtil.setRefreshThemeColors(
            vb.refreshLayout, R.attr.colorPrimary, R.attr.colorAccent )
        vb.refreshLayout.setOnRefreshListener {
            vm.refresh()
        }
    }


    private fun renderRefreshState(state: RefreshState) {
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
            R.string.refresh_needs_internet_msg
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


    private fun renderChangeRelationState(state: ChangeRelationRequestState) {
        isSendingConnectRequest = (state is ChangeRelationRequestState.Sending)
        updateProgressBarVisibility()

        if(state is ChangeRelationRequestState.NetworkError && !state.notified) {
            showErrorDialog(
                R.string.send_connect_request_failed_title,
                R.string.network_error_msg
            ) { state.notified = true }
        }
    }


    private fun updateProgressBarVisibility() {
        vb.prBar.isVisible = isSyncing || isSendingConnectRequest
    }


    internal abstract
    fun provideContainerDescriptor(): CoPrisonersContainerDescriptor<ViewModelType>
}