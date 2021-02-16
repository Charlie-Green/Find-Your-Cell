package by.zenkevich_churun.findcell.result.ui.request.income

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class IncomingRequestsPageDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersPageDescriptor<IncomingRequestsViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_requests_empty


    override fun label1(relation: CoPrisoner.Relation)
        = R.string.cpoption_confirm

    override fun label2(relation: CoPrisoner.Relation): Int
        = R.string.cpoption_decline

    override fun onSelected1(
        vm: IncomingRequestsViewModel,
        relation: CoPrisoner.Relation,
        position: Int ) {

        vm.confirmRequest(position)
    }

    override fun onSelected2(
        vm: IncomingRequestsViewModel,
        relation: CoPrisoner.Relation,
        position: Int ) {

        vm.declineRequest(position)
    }


    override fun getViewModel(
        appContext: Context
    ) = IncomingRequestsViewModel.get(appContext, vmOwner)
}


class IncomingRequestsPage: CoPrisonersPageFragment<IncomingRequestsViewModel>() {

    override fun providePageDescriptor():
    CoPrisonersPageDescriptor<IncomingRequestsViewModel> {
        return IncomingRequestsPageDescriptor(this)
    }
}