package by.zenkevich_churun.findcell.result.repo.sync

import android.util.Log
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationScheduler
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizedDataManager
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SynchronizationRepositoryImpl @Inject constructor(
    private val scheduler: SynchronizationScheduler,
    private val dataMan: SynchronizedDataManager
): SynchronizationRepository {

    override fun sync(): Boolean {

        val success = try {
            dataMan.sync()
            true
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Sync failed: ${exc.javaClass.name}: ${exc.message}")
            false
        }

        scheduler.notifySynchronizationFinished(success)
        return success
    }


    companion object {
        private const val LOGTAG = "FindCell-Sync"
    }
}