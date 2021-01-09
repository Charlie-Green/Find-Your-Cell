package by.zenkevich_churun.findcell.core.injected.sync

import androidx.lifecycle.LiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** Coordinates [SynchronizationScheduler] and [SynchronizedDataManager]. **/
interface SynchronizationRepository {

    val syncStateLD: LiveData<SyncState>

    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]
      * and [CoPrisoner.Relation.OUTCOMING_REQUEST]. **/
    val suggestedLD: LiveData< List<CoPrisoner> >

    /** [CoPrisoner]s with [CoPrisoner.Relation.CONNECTED]. **/
    val connectedLD: LiveData< List<CoPrisoner> >

    /** [CoPrisoner]s with [CoPrisoner.Relation.INCOMING_REQUEST]. **/
    val requestsLD: LiveData< List<CoPrisoner> >

    /** Sync right now. **/
    fun forceSync(): SyncResponse

    /** Check business-requirements for sync, and if they hold, perform sync. **/
    fun sync(): SyncResponse
}