package by.zenkevich_churun.findcell.result.ui.request.outcome

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import javax.inject.Inject


class OutcomingRequestsViewModel @Inject constructor(
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {

    override val dataSource: LiveData<List<CoPrisoner>>
        get() = cpRepo.outcomingRequestsLD(viewModelScope)

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