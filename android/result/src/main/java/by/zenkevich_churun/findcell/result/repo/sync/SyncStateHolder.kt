package by.zenkevich_churun.findcell.result.repo.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.injected.sync.SyncState


internal class SyncStateHolder {

    private val mldState = MutableLiveData<SyncState>()

    val stateLD: LiveData<SyncState>
        get() = mldState


    /** Checks if sync is currently running and, if it's now,
      * cnanges [SyncState] to [SyncState.InProgress] atomically.
      * @return whether [SyncState] was changed and sync is supposed to run. **/
    fun checkAndRunSync(): Boolean {
        synchronized(mldState) {
            if(mldState.value is SyncState.InProgress) {
                return false
            }

            mldState.postValue(SyncState.InProgress)
            return true
        }
    }

    fun notifySyncFinished(success: Boolean, lastSuccessTime: Long) {
        val newState = SyncState.NotSyncing(lastSuccessTime, !success)
        synchronized(mldState) {
            mldState.postValue(newState)
        }
    }
}