package by.zenkevich_churun.findcell.result.ui.request.outcome

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageDescriptor
import by.zenkevich_churun.findcell.result.ui.shared.cppage.fragm.CoPrisonersPageFragment


private class OutcomingRequestsPageDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersPageDescriptor<OutcomingRequestsViewModel> {

    override val emptyLabelRes: Int
        get() = R.string.cp_requests_empty


    override fun label1(cp: CoPrisoner)
        = R.string.cpoption_cancel_request

    override fun onSelected1(
        vm: OutcomingRequestsViewModel,
        cp: CoPrisoner,
        position: Int ) {

        vm.cancelRequest(position)
    }


    override fun getViewModel(
        appContext: Context
    ) = OutcomingRequestsViewModel.get(appContext, vmOwner)

}


class OutcomingRequestsPage: CoPrisonersPageFragment<OutcomingRequestsViewModel>() {

    override fun providePageDescriptor():
    CoPrisonersPageDescriptor<OutcomingRequestsViewModel> {
        return OutcomingRequestsPageDescriptor(this)
    }
}