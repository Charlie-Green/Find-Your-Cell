package by.zenkevich_churun.findcell.result.ui.shared.cps

import android.util.Log
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.injected.sync.SyncResponse
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.result.ui.cps.model.RefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/** Helper class to refresh [CoPrisoner]s.
  * This class's level in the app's architecture is the ViewModel level. **/
internal class CoPrisonersRefreshVMHelper(
    private val syncRepo: SynchronizationRepository,
    private val mldRefreshState: MutableLiveData<RefreshState>,
    private val netTracker: NetworkStateTracker ) {

    fun refresh(scope: CoroutineScope) {
        if(mldRefreshState.value is RefreshState.InProgress) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            mldRefreshState.postValue(RefreshState.NoInternet())
            return
        }

        mldRefreshState.value = RefreshState.InProgress
        scope.launch(Dispatchers.IO) {
            val response = syncRepo.forceSync()
            changeRefreshState(response)
        }
    }


    private fun changeRefreshState(response: SyncResponse) {
        Log.v("CharlieDebug", "response = ${response.name}")
        val newState = when(response) {
            SyncResponse.SUCCESS -> RefreshState.NotRefreshing
            SyncResponse.IGNORED -> RefreshState.NotRefreshing
            SyncResponse.ERROR   -> RefreshState.NetworkError()
        }

        mldRefreshState.postValue(newState)
    }
}