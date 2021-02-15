package by.zenkevich_churun.findcell.result.ui.suggest.fragm

import android.content.Context
import android.os.Bundle
import android.view.View
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.shared.cppage.model.ChangeRelationRequestState
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


    override val emptyLabelRes: Int
        get() = R.string.cp_suggested_empty

    override fun obtainViewModel(
        appContext: Context
    ) = SuggestedCoPrisonersViewModel.get(appContext, this)

    override fun provideOptionsAdapter(
        vm: SuggestedCoPrisonersViewModel
    ): CoPrisonerOptionsAdapter = SuggestedCoPrisonerOptionsAdapter(vm)


    private fun consumeState(state: ChangeRelationRequestState) {
        if(state is ChangeRelationRequestState.Success && !state.notified) {
            state.notified = true
            notifyCoPrisonerChanged(state.updatedPosition)
        }
    }
}