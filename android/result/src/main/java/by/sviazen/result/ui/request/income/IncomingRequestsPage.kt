package by.sviazen.result.ui.request.income

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.R
import by.sviazen.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.sviazen.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class IncomingRequestsPageDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersPageDescriptor<IncomingRequestsViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_requests_empty


    override fun label1(cp: CoPrisoner): Int {
        return R.string.cpoption_confirm
    }

    override fun label2(cp: CoPrisoner): Int {
        return if(cp.relation == CoPrisoner.Relation.REQUEST_DECLINED) 0
            else R.string.cpoption_decline
    }

    override fun onSelected1(
        vm: IncomingRequestsViewModel,
        cp: CoPrisoner,
        position: Int ) {

        vm.confirmRequest(position)
    }

    override fun onSelected2(
        vm: IncomingRequestsViewModel,
        cp: CoPrisoner,
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