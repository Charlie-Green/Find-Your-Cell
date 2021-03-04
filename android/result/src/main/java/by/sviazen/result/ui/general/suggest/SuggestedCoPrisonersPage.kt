package by.sviazen.result.ui.general.suggest

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.R
import by.sviazen.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.sviazen.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class SuggestedPageDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersPageDescriptor<SuggestedCoPrisonersViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_suggested_empty


    override fun label1(cp: CoPrisoner): Int {
        if(cp.relation == CoPrisoner.Relation.SUGGESTED) {
            return R.string.cpoption_connect
        }
        return R.string.cpoption_cancel_request
    }

    override fun onSelected1(
        vm: SuggestedCoPrisonersViewModel,
        cp: CoPrisoner,
        position: Int ) {

        if(cp.relation == CoPrisoner.Relation.SUGGESTED) {
            return vm.sendConnectRequest(position)
        }
        vm.cancelConnectRequest(position)
    }


    override fun getViewModel(
        appContext: Context
    ) = SuggestedCoPrisonersViewModel.get(appContext, vmOwner)
}


class SuggestedCoPrisonersPage: CoPrisonersPageFragment<SuggestedCoPrisonersViewModel>() {

    override fun providePageDescriptor():
    CoPrisonersPageDescriptor<SuggestedCoPrisonersViewModel> {
        return SuggestedPageDescriptor(this)
    }
}