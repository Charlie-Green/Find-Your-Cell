package by.zenkevich_churun.findcell.core.injected.sync

import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.domain.entity.CoPrisoner


/** Coordinates [SynchronizationScheduler] and [SynchronizedDataManager]. **/
interface SynchronizationRepository {

    val syncStateLD: LiveData<SyncState>

    /** This is for [CoPrisoner]s screens when they get visible.
      * Checks if an automatic sync should run. **/
    val shouldAutoSync: Boolean

    /** Sync right now. **/
    fun forceSync(): SyncResponse

    /** Check business-requirements for sync, and if they hold, perform sync. **/
    fun sync(): SyncResponse

    /** Start an automatic sync, if it should run. **/
    fun autoSync()
}