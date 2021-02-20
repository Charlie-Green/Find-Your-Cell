package by.zenkevich_churun.findcell.result.repo.sync

import by.zenkevich_churun.findcell.core.injected.sync.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton


/** This class's functionality is small yet is used from different places
  * within the app, which is why it's separated into a standalone class. **/
@Singleton
class AutomaticSyncManagerImpl @Inject constructor(
    private val scheduler: SynchronizationScheduler,
    private val dataMan: SynchronizedDataManager
): AutomaticSyncManager {

    private val flag = AtomicBoolean(true)


    override fun get(): Boolean {
        return flag.get()
    }

    override fun consume(): Boolean {
        return flag.getAndSet(false)
    }

    override fun set(value: Boolean) {
        val oldValue = flag.getAndSet(value)
        if(!oldValue) {
            scheduler.cancelSyncs()
        }
    }

    override fun clearCoPrisonersCache() {
        dataMan.clear()
    }
}