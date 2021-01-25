package by.zenkevich_churun.findcell.result.ui.suggest.fragm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestState
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonerOptionsAdapter
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPage
import by.zenkevich_churun.findcell.result.ui.suggest.vm.SuggestedCoPrisonersViewModel


class SuggestedCoPrisonersPage: CoPrisonersPage<SuggestedCoPrisonersViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.connectRequestStateLD.observe(viewLifecycleOwner) { state ->
            consumeState(state)
        }
    }


    override fun obtainViewModel(
        appContext: Context
    ) = SuggestedCoPrisonersViewModel.get(appContext, this)

    override fun provideOptionsAdapter(
        vm: SuggestedCoPrisonersViewModel
    ): CoPrisonerOptionsAdapter = SuggestedCoPrisonerOptionsAdapter(vm)


    private fun consumeState(state: ConnectRequestState) {
        if(state is ConnectRequestState.Success && !state.notified) {
            state.notified = true
            notifyCoPrisonerChanged(state.updatedPosition)
        }
    }
}