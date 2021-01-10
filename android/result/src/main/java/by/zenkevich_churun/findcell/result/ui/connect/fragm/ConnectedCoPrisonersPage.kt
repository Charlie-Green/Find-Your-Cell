package by.zenkevich_churun.findcell.result.ui.connect.fragm

import android.content.Context
import by.zenkevich_churun.findcell.result.ui.connect.vm.ConnectedCoPrisonersViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonerOptionsAdapter
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPage


class ConnectedCoPrisonersPage: CoPrisonersPage<ConnectedCoPrisonersViewModel>() {

    override fun obtainViewModel(
        appContext: Context
    ) = ConnectedCoPrisonersViewModel.get(appContext, this)

    override fun provideOptionsAdapter(
        vm: ConnectedCoPrisonersViewModel
    ): CoPrisonerOptionsAdapter = ConnectedCoPrisonerOptionsAdapter()
}