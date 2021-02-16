package by.zenkevich_churun.findcell.result.ui.request.container

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.result.R
import by.zenkevich_churun.findcell.result.ui.request.income.IncomingRequestsPage
import by.zenkevich_churun.findcell.result.ui.request.outcome.OutcomingRequestsPage
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.fragm.CoPrisonersContainerDescriptor
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.fragm.CoPrisonersContainerFragment


private const val INDEX_INCOME  = 0
private const val INDEX_OUTCOME = 1


private class ConnectContainerDescriptor(
    private val vmOwner: ViewModelStoreOwner
): CoPrisonersContainerDescriptor<ConnectRequestsViewModel> {

    override val tabCount: Int
        get() = 2

    override fun tabLabelRes(index: Int) = when(index) {
        INDEX_INCOME -> R.string.income_requests_tabtext
        else         -> R.string.outcome_requests_tabtext
    }

    override fun createPage(index: Int) = when(index) {
        INDEX_INCOME -> IncomingRequestsPage()
        else         -> OutcomingRequestsPage()
    }


    override fun getViewModel(
        appContext: Context
    ) = ConnectRequestsViewModel.get(appContext, vmOwner)
}


class ConnectRequestsFragment: CoPrisonersContainerFragment<ConnectRequestsViewModel>() {

    override fun provideContainerDescriptor():
    CoPrisonersContainerDescriptor<ConnectRequestsViewModel> {
        return ConnectContainerDescriptor(this)
    }
}