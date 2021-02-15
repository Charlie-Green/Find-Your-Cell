package by.zenkevich_churun.findcell.result.ui.suggest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cppage.model.ChangeRelationRequestState
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import javax.inject.Inject


class SuggestedCoPrisonersViewModel @Inject constructor(
    repo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(repo, changeRelationStore, netTracker) {


    override val dataSource: LiveData<List<CoPrisoner>>
        get() = cpRepo.suggestedLD(viewModelScope)

    public val changeRelationRequestStateLD: LiveData<ChangeRelationRequestState>
        get() = super.changeRelationRequestStateLD


    fun sendConnectRequest(position: Int) {
        connect(position)
    }

    fun cancelConnectRequest(position: Int) {
        disconnect(position)
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): SuggestedCoPrisonersViewModel {

            val fact = SuggestedCoPrisonersVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(SuggestedCoPrisonersViewModel::class.java)
        }
    }
}