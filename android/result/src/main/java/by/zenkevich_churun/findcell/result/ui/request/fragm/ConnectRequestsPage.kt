package by.zenkevich_churun.findcell.result.ui.request.fragm

import android.content.Context
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.request.vm.ConnectRequestsViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonerOptionsAdapter
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPage


internal class ConnectRequestsPage: CoPrisonersPage<ConnectRequestsViewModel>() {

    override val emptyLabelRes: Int
        get() = R.string.cp_requests_empty

    override fun obtainViewModel(
        appContext: Context
    ) = ConnectRequestsViewModel.get(appContext, this)

    override fun provideOptionsAdapter(
        vm: ConnectRequestsViewModel
    ): CoPrisonerOptionsAdapter = ConnectRequestOptionsAdapter(vm)


    public val viewModel
        get() = vm
}