package by.zenkevich_churun.findcell.result.ui.cps.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.sync.SyncResponse
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.cps.model.RefreshState
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestState
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersRefreshVMHelper
import by.zenkevich_churun.findcell.result.ui.shared.sync.SyncVMHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CoPrisonersViewModel @Inject constructor(
    private val syncRepo: SynchronizationRepository,
    private val cpRepo: CoPrisonersRepository,
    private val netTracker: NetworkStateTracker,
    private val connectRequestStore: ConnectRequestLiveDataStorage
): ViewModel() {

    private val mldRefreshState = MutableLiveData<RefreshState>().apply {
        value = RefreshState.NotRefreshing
    }

    private val refreshHelper
        = CoPrisonersRefreshVMHelper(syncRepo, mldRefreshState, netTracker)


    val showSyncLD: LiveData<Boolean> by lazy {
        ShowSyncMediatorLiveData(syncRepo, cpRepo, viewModelScope)
    }

    val refreshStateLD: LiveData<RefreshState>
        get() = mldRefreshState

    val sendConnectRequestStateLD: LiveData<ConnectRequestState>
        get() = connectRequestStore.stateLD



    fun refresh()
        = refreshHelper.refresh(viewModelScope)

    fun onViewCreated() {
        SyncVMHelper.triggerSync(syncRepo, netTracker, viewModelScope)
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CoPrisonersViewModel {

            val fact = CoPrisonersVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CoPrisonersViewModel::class.java)
        }
    }
}