package by.zenkevich_churun.findcell.result.sync.scheduler

import android.content.Context
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


    override fun notifyProfileUpdated() {
        Log.v("CharlieDebug", "Profile Updated")
    }

    override fun notifySynchronizationFinished(success: Boolean) {
        Log.v("CharlieDebug", "Synchronization Finished. Success: $success")
    }


    override fun syncSoon() {
        workWrapper.scheduleWork(15_000L)
    }

    override fun cancelSync() {
        workWrapper.cancelWork()
    }
}