package by.sviazen.result.ui.shared.cpcontainer.vm

import androidx.lifecycle.*
import by.sviazen.core.injected.sync.SyncResponse
import by.sviazen.core.injected.sync.SynchronizationRepository
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.injected.cp.CoPrisonersRepository
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.ui.shared.cpcontainer.model.RefreshState
import by.sviazen.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.sviazen.result.ui.shared.cppage.model.ChangeRelationRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/** Super class for [CoPrisoner]s container screens' [ViewModel]s. **/
abstract class CoPrisonersContainerViewModel(
    protected val syncRepo: SynchronizationRepository,
    protected val cpRepo: CoPrisonersRepository,
    protected val netTracker: NetworkStateTracker,
    protected val changeRelationStore: ChangeRelationLiveDataStorage
): ViewModel() {

    private val mldRefreshState = MutableLiveData<RefreshState>().apply {
        value = RefreshState.NotRefreshing
    }

    val showSyncLD: LiveData<Boolean> by lazy {
        ShowSyncMediatorLiveData(syncRepo, dataSource1, dataSource2)
    }

    val refreshStateLD: LiveData<RefreshState>
        get() = mldRefreshState

    val changeRelationStateLD: LiveData<ChangeRelationRequestState>
        get() = changeRelationStore.stateLD


    fun refresh() {
        if(mldRefreshState.value is RefreshState.InProgress) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            mldRefreshState.postValue(RefreshState.NoInternet())
            return
        }

        mldRefreshState.value = RefreshState.InProgress
        viewModelScope.launch(Dispatchers.IO) {
            val response = syncRepo.forceSync()
            changeRefreshState(response)
        }
    }

    fun onViewCreated() {
        if(!syncRepo.shouldAutoSync) {
            return
        }

        netTracker.doOnAvailable {
            viewModelScope.launch(Dispatchers.IO) {
                syncRepo.autoSync()
            }
        }
    }


    private fun changeRefreshState(response: SyncResponse) {
        val newState = when(response) {
            SyncResponse.SUCCESS -> RefreshState.NotRefreshing
            SyncResponse.IGNORED -> RefreshState.NotRefreshing
            SyncResponse.ERROR   -> RefreshState.NetworkError()
        }

        mldRefreshState.postValue(newState)
    }


    protected abstract val dataSource1: LiveData< out List<CoPrisoner> >
    protected abstract val dataSource2: LiveData< out List<CoPrisoner> >
}