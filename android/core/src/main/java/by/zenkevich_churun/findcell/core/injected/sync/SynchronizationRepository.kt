package by.zenkevich_churun.findcell.core.injected.sync

import androidx.lifecycle.LiveData


/** Coordinates [SynchronizationScheduler] and [SynchronizedDataManager]. **/
interface SynchronizationRepository {

    val syncStateLD: LiveData<SyncState>

    /** Sync right now. **/
    fun forceSync(): SyncResponse

    /** Check business-requirements for sync, and if they hold, perform sync. **/
    fun sync(): SyncResponse
}