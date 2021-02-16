package by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zenkevich_churun.findcell.core.injected.sync.SyncResponse
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.model.RefreshState
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cppage.model.ChangeRelationRequestState
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
        ShowSyncMediatorLiveData(syncRepo, cpRepo, viewModelScope)
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


    private fun changeRefreshState(response: SyncResponse) {
        val newState = when(response) {
            SyncResponse.SUCCESS -> RefreshState.NotRefreshing
            SyncResponse.IGNORED -> RefreshState.NotRefreshing
            SyncResponse.ERROR   -> RefreshState.NetworkError()
        }

        mldRefreshState.postValue(newState)
    }


    companion object {
        private var syncTriggered = false
    }
}