package by.zenkevich_churun.findcell.result.repo.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.injected.sync.SyncState


internal class SyncStateHolder {

    private val mldState = MutableLiveData<SyncState>()

    val stateLD: LiveData<SyncState>
        get() = mldState


    fun notifySyncRan() {
        mldState.postValue(SyncState.InProgress)
    }

    fun notifySyncFinished(success: Boolean, lastSuccessTime: Long) {
        val newState = SyncState.NotSyncing(lastSuccessTime, !success)
        mldState.postValue(newState)
    }
}