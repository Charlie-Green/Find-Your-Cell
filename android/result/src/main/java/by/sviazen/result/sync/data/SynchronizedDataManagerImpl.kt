package by.sviazen.result.sync.data

import android.content.Context
import by.sviazen.core.api.sync.SynchronizationApi
import by.sviazen.core.common.prisoner.PrisonerStorage
import by.sviazen.core.injected.db.JailsCache
import by.sviazen.core.injected.sync.SynchronizedDataManager
import by.sviazen.domain.contract.cp.CoPrisonerHeaderPojo
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.db.CoPrisonersDatabase
import by.sviazen.result.db.entity.CoPrisonerEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SynchronizedDataManagerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val prisonerStore: PrisonerStorage,
    private val api: SynchronizationApi,
    private val jailsCache: JailsCache
): SynchronizedDataManager {

    override fun sync() {
        val prisoner = prisonerStore.prisonerLD.value ?: return
        val data = api.fetchData(prisoner.id, prisoner.passwordHash!!)
        cache(data.coPrisoners)
        jailsCache.cache(data.jails)
    }

    override fun clear() {
        db.dao.deleteCoPrisoners()
    }


    private val db
        get() = CoPrisonersDatabase.get(appContext)

    private fun cache(coPrisoners: List<CoPrisonerHeaderPojo>) {
        val dao = db.dao

        val coPrisonerEntities = coPrisoners.map { cp ->
            CoPrisonerEntity.from(cp)
        }

        db.runInTransaction {
            dao.deleteCoPrisoners()
            dao.addOrUpdateCoprisoners(coPrisonerEntities)
        }
    }
}