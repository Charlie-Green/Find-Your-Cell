package by.sviazen.result.sync.scheduler

import android.content.Context
import androidx.work.*
import by.sviazen.core.repo.sync.SynchronizationRepository
import by.sviazen.core.injected.web.NetworkStateTracker
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import java.util.concurrent.TimeUnit


internal class WorkManagerWrapper(private val appContext: Context) {

    fun scheduleWork(delay: Long) {
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
        val syncRepoForWork: SynchronizationRepository
        val netTrackerForWork: NetworkStateTracker
    }


    class SyncWorker(
        context: Context,
        params: WorkerParameters
    ): Worker(context, params) {

        private lateinit var repo: SynchronizationRepository
        private lateinit var netTracker: NetworkStateTracker


        override fun doWork(): Result {
            initFields()
            netTracker.doOnAvailable(repo::sync)
            return Result.success()
        }


        private fun initFields() {
            val entryClass = WorkManagerEntryPoint::class.java
            val deps = EntryPointAccessors.fromApplication(applicationContext, entryClass)
            repo = deps.syncRepoForWork
            netTracker = deps.netTrackerForWork
        }
    }


    companion object {
        private const val WORK_NAME = "SyncWork"
    }
}