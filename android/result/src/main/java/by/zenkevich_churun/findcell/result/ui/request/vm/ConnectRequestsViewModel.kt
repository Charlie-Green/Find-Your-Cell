package by.zenkevich_churun.findcell.result.ui.request.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.cps.model.RefreshState
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPageViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersRefreshVMHelper
import javax.inject.Inject


class ConnectRequestsViewModel @Inject constructor(
    syncRepo: SynchronizationRepository,
    repo: CoPrisonersRepository,
    connectRequestStore: ConnectRequestLiveDataStorage,
    netTracker: NetworkStateTracker
): CoPrisonersPageViewModel(repo, connectRequestStore, netTracker) {

    private val mldRefreshState = MutableLiveData<RefreshState>().apply {
        value = RefreshState.NOT_REFRESHING
    }

    private val refreshHelper
        = CoPrisonersRefreshVMHelper(syncRepo, mldRefreshState, netTracker)


    override val dataSource: LiveData<List<CoPrisoner>>
        get() = repo.requestsLD(viewModelScope)


    val refreshStateLD: LiveData<RefreshState>
        get() = mldRefreshState

    fun refresh()
        = refreshHelper.refresh(viewModelScope)

    fun confirmRequest(position: Int)
        = connect(position)

    fun declineRequest(position: Int)
        = disconnect(position)


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ConnectRequestsViewModel {

            val fact = ConnectRequestsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ConnectRequestsViewModel::class.java)
        }
    }
}