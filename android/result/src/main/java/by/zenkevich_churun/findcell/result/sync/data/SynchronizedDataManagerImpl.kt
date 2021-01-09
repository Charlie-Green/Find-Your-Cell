package by.zenkevich_churun.findcell.result.sync.data

import by.zenkevich_churun.findcell.core.api.sync.SynchronizationApi
import by.zenkevich_churun.findcell.core.common.prisoner.PrisonerStorage
import by.zenkevich_churun.findcell.core.injected.db.JailsCache
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizedDataManager
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SynchronizedDataManagerImpl @Inject constructor(
    private val prisonerStore: PrisonerStorage,
    private val api: SynchronizationApi,
    private val jailsCache: JailsCache
): SynchronizedDataManager {

    override fun sync() {
        val prisoner = prisonerStore.prisonerLD.value ?: return
        val data = api.fetchData(prisoner.id, prisoner.passwordHash)
        cache(data.coPrisoners)
        jailsCache.cache(data.jails)
    }


    private fun cache(coPrisoners: List<CoPrisoner>) {

    }
}