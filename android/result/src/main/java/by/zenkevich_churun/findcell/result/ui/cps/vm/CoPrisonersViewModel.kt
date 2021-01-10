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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CoPrisonersViewModel @Inject constructor(
    private val syncRepo: SynchronizationRepository,
    private val cpRepo: CoPrisonersRepository,
    private val netTracker: NetworkStateTracker,
    private val connectRequestStore: ConnectRequestLiveDataStorage
): ViewModel() {

    private var syncTriggered = false
    private val mldRefreshState = MutableLiveData<RefreshState>().apply {
        value = RefreshState.NOT_REFRESHING
    }


    val showSyncLD: LiveData<Boolean> by lazy {
        ShowSyncMediatorLiveData(syncRepo, cpRepo, viewModelScope)
    }

    val refreshStateLD: LiveData<RefreshState>
        get() = mldRefreshState

    val sendConnectRequestStateLD: LiveData<ConnectRequestState>
        get() = connectRequestStore.stateLD


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

    fun refresh() {
        if(mldRefreshState.value != RefreshState.NOT_REFRESHING) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            mldRefreshState.postValue(RefreshState.NO_INTERNET)
            return
        }

        mldRefreshState.value = RefreshState.REFRESHING
        viewModelScope.launch(Dispatchers.IO) {
            val response = syncRepo.forceSync()
            changeRefreshState(response)
        }
    }

    fun onRefreshStateNotified() {
        if(mldRefreshState.value == RefreshState.ERROR ||
            mldRefreshState.value == RefreshState.NO_INTERNET ) {

            mldRefreshState.value = RefreshState.NOT_REFRESHING
        }
    }


    private fun changeRefreshState(response: SyncResponse) {
        Log.v("CharlieDebug", "SyncResponse = ${response.name}")
        val newState = when(response) {
            SyncResponse.SUCCESS -> RefreshState.NOT_REFRESHING
            SyncResponse.IGNORED -> RefreshState.NOT_REFRESHING
            SyncResponse.ERROR   -> RefreshState.ERROR
        }

        mldRefreshState.postValue(newState)
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