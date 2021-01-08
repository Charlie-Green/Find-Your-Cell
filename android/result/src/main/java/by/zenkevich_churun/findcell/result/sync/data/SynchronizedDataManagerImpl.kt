package by.zenkevich_churun.findcell.result.sync.data

import by.zenkevich_churun.findcell.core.injected.sync.SynchronizedDataManager
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SynchronizedDataManagerImpl
@Inject constructor(): SynchronizedDataManager {

    override fun sync() {
        Thread.sleep(3000L)
    }
}