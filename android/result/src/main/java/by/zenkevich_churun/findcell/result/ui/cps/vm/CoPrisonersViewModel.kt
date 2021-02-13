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
        value = RefreshState.NOT_REFRESHING
    }

    private val refreshHelper
        = CoPrisonersRefreshVMHelper(syncRepo, mldRefreshState, netTracker)

    private var syncTriggered = false


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
        if(syncTriggered) {
            return
        }
        syncTriggered = true

        netTracker.doOnAvailable {
            viewModelScope.launch(Dispatchers.IO) {
                syncRepo.sync()
            }
        }
    }

    fun onRefreshStateNotified() {
        if(mldRefreshState.value == RefreshState.ERROR ||
            mldRefreshState.value == RefreshState.NO_INTERNET ) {

            mldRefreshState.value = RefreshState.NOT_REFRESHING
        }
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