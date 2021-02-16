package by.zenkevich_churun.findcell.result.ui.general.suggest

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class SuggestedPageDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersPageDescriptor<SuggestedCoPrisonersViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_suggested_empty


    override fun label1(relation: CoPrisoner.Relation): Int {
        if(relation == CoPrisoner.Relation.SUGGESTED) {
            return R.string.cpoption_connect
        }
        return R.string.cpoption_cancel_request
    }

    override fun onSelected1(
        vm: SuggestedCoPrisonersViewModel,
        relation: CoPrisoner.Relation,
        position: Int ) {

        if(relation == CoPrisoner.Relation.OUTCOMING_REQUEST) {
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