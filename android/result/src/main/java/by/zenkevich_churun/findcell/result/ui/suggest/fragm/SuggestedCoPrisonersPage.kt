package by.zenkevich_churun.findcell.result.ui.suggest.fragm

import android.content.Context
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonerOptionsAdapter
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPage
import by.zenkevich_churun.findcell.result.ui.suggest.vm.SuggestedCoPrisonersViewModel


class SuggestedCoPrisonersPage: CoPrisonersPage<SuggestedCoPrisonersViewModel>() {

    override fun obtainViewModel(
        appContext: Context
    ) = SuggestedCoPrisonersViewModel.get(appContext, this)

    override fun provideOptionsAdapter(
        vm: SuggestedCoPrisonersViewModel
    ): CoPrisonerOptionsAdapter = SuggestedCoPrisonerOptionsAdapter(vm)
}