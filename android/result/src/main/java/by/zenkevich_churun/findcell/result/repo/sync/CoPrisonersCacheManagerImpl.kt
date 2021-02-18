package by.zenkevich_churun.findcell.result.repo.sync

import android.util.Log
import by.zenkevich_churun.findcell.core.injected.sync.*
import javax.inject.Inject
import javax.inject.Singleton


/** This class's functionality is small yet is used from different places
  * within the app, which is why it's separated into a standalone class. **/
@Singleton
class CoPrisonersCacheManagerImpl @Inject constructor(
    private val scheduler: SynchronizationScheduler,
    private val dataMan: SynchronizedDataManager
): CoPrisonersCacheManager {

    override fun invalidate() {
        Log.v("CharlieDebug", "CoPrisoners cleared")
        dataMan.clear()
        scheduler.cancelSyncs()
    }

    override fun scheduleFirstSync() {
        scheduler.scheduleFirstSync()
    }
}