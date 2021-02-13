package by.zenkevich_churun.findcell.result.ui.suggest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestState
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPageViewModel
import javax.inject.Inject


class SuggestedCoPrisonersViewModel @Inject constructor(
    repo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    connectRequestStore: ConnectRequestLiveDataStorage
): CoPrisonersPageViewModel(repo, connectRequestStore, netTracker) {


    override val dataSource: LiveData<List<CoPrisoner>>
        get() = repo.suggestedLD(viewModelScope)

    override public val connectRequestStateLD: LiveData<ConnectRequestState>
        get() = super.connectRequestStateLD


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