package by.zenkevich_churun.findcell.result.ui.connect.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import javax.inject.Inject


class ConnectedCoPrisonersViewModel @Inject constructor(
    repo: CoPrisonersRepository,
    changeRelationStore: ChangeRelationLiveDataStorage,
    netTracker: NetworkStateTracker
): CoPrisonersPageViewModel(repo, changeRelationStore, netTracker) {

    override val dataSource: LiveData<List<CoPrisoner>>
        get() = cpRepo.connectedLD(viewModelScope)


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ConnectedCoPrisonersViewModel {

            val fact = ConnectedCoPrisonersVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ConnectedCoPrisonersViewModel::class.java)
        }
    }
}