package by.sviazen.result.ui.request.outcome

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.injected.cp.CoPrisonersRepository
import by.sviazen.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.sviazen.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import javax.inject.Inject


class OutcomingRequestsViewModel @Inject constructor(
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {

    override fun dataSource()
        = cpRepo.outcomingRequestsLD


    fun cancelRequest(position: Int)
        = disconnect(position)


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): OutcomingRequestsViewModel {

            val fact = OutcomingRequestsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(OutcomingRequestsViewModel::class.java)
        }
    }
}