package by.sviazen.result.repo.sync

import android.util.Log
import androidx.lifecycle.LiveData
import by.sviazen.core.injected.sync.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SynchronizationRepositoryImpl @Inject constructor(
    private val scheduler: SynchronizationScheduler,
    private val dataMan: SynchronizedDataManager,
    private val autoSyncMan: AutomaticSyncManager
): SynchronizationRepository {

    private val stateHolder = SyncStateHolder()


    override val syncStateLD: LiveData<SyncState>
        get() = stateHolder.stateLD

    override val shouldAutoSync: Boolean
        get() = autoSyncMan.get()


    override fun forceSync(): SyncResponse {
        if(!stateHolder.checkAndRunSync()) {
            return SyncResponse.IGNORED
        }

        scheduler.notifySyncRan()

        val success = try {
            dataMan.sync()
            true
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Sync failed: ${exc.javaClass.name}: ${exc.message}")
            false
        }

        scheduler.notifySyncFinished(success)
        stateHolder.notifySyncFinished(success, scheduler.lastSucessfulSyncTime)

        return if(success) SyncResponse.SUCCESS else SyncResponse.ERROR
    }

    override fun sync(): SyncResponse {
        if(!scheduler.isTimeToSync) {
            return SyncResponse.IGNORED
        }
        return forceSync()
    }

    override fun autoSync() {
        val shouldSync = autoSyncMan.consume()
        if(shouldSync) {
            forceSync()
        }
    }


    companion object {
        private const val LOGTAG = "FindCell-Sync"
    }
}