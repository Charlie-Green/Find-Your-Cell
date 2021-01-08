package by.zenkevich_churun.findcell.result.sync.scheduler

import android.content.Context
import android.os.SystemClock
import android.util.Log
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SynchronizationSchedulerImpl @Inject constructor(
    @ApplicationContext appContext: Context
): SynchronizationScheduler {

    private val workWrapper = WorkManagerWrapper(appContext)
    private val metaStorage = SyncMetaStorage(appContext)


    override val lastSucessfulSyncTime: Long
        get() = metaStorage.lastSuccessfulSyncTime

    override val isTimeToSync: Boolean
        get() {
            val lastSync = metaStorage.lastSyncTime
            val now = SystemClock.elapsedRealtime()
            Log.v("CharlieDebug", "now = ${now/1000L}, lastSync=${lastSync}")

            if(now < lastSync) {
                // Device has been rebooted, or for another reason, the time is invalid.
                return true
            }

            return (now - lastSync) >= SYNC_INTERVAL
        }

    override fun notifyArestUpdated() {
        Log.v("CharlieDebug", "Arest Updated")
        // Invalidate the previous sync:
        metaStorage.lastSyncTime = SystemClock.elapsedRealtime() - SYNC_INTERVAL
    }

    override fun notifySyncRan() {
        Log.v("CharlieDebug", "Synchronization ran")
        workWrapper.cancelWork()
    }

    override fun notifySyncFinished(success: Boolean) {
        Log.v("CharlieDebug", "Synchronization Finished. Success: $success")

        metaStorage.lastSyncTime = SystemClock.elapsedRealtime()
        if(success) {
            metaStorage.lastSuccessfulSyncTime = System.currentTimeMillis()
        }

        workWrapper.scheduleWork(SYNC_INTERVAL)
    }


    companion object {
        // TODO: Set to 28_800_000L
        private const val SYNC_INTERVAL = 60_000L
    }
}