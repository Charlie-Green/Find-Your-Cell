package by.zenkevich_churun.findcell.result.sync.scheduler

import android.content.Context
import android.util.Log
import androidx.work.*
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import java.util.concurrent.TimeUnit


internal class WorkManagerWrapper(private val appContext: Context) {

    fun scheduleWork(delay: Long) {
        Log.v("CharlieDebug", "Schedule work in ${delay/1000L}s")

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workMan.enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelWork() {
        workMan.cancelUniqueWork(WORK_NAME)
    }


    private val workMan: WorkManager
        get() = WorkManager.getInstance(appContext)


    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface WorkManagerEntryPoint {
        val syncRepo: SynchronizationRepository
    }


    class SyncWorker(
        context: Context,
        params: WorkerParameters
    ): Worker(context, params) {

        override fun doWork(): Result {
            Log.v("CharlieDebug", "Sync started.")
            val repo = obtainRepository()
            val synced = repo.sync()
            return if(synced) Result.success() else Result.failure()
        }


        private fun obtainRepository(): SynchronizationRepository {
            val entryClass = WorkManagerEntryPoint::class.java
            return EntryPointAccessors
                .fromApplication(applicationContext, entryClass)
                .syncRepo
        }
    }


    companion object {
        private const val WORK_NAME = "SyncWork"
    }
}