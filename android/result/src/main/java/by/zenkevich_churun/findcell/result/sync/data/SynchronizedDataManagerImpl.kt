package by.zenkevich_churun.findcell.result.sync.data

import android.content.Context
import by.zenkevich_churun.findcell.core.api.sync.SynchronizationApi
import by.zenkevich_churun.findcell.core.common.prisoner.PrisonerStorage
import by.zenkevich_churun.findcell.core.injected.db.JailsCache
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizedDataManager
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.db.CoPrisonersDatabase
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerContactEntity
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity
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
        val data = api.fetchData(prisoner.id, prisoner.passwordHash)
        cache(data.coPrisoners)
        jailsCache.cache(data.jails)
    }


    private fun cache(coPrisoners: List<CoPrisoner>) {
        val db = CoPrisonersDatabase.get(appContext)
        val dao = db.dao

        val coPrisonerEntities = coPrisoners.map { cp ->
            CoPrisonerEntity.from(cp)
        }

        val contactEntities = mutableListOf<CoPrisonerContactEntity>()
        for(coPrisoner in coPrisoners) {
            for(contact in coPrisoner.contacts) {
                val entity = CoPrisonerContactEntity.from(contact, coPrisoner.id)
                contactEntities.add(entity)
            }
        }

        db.runInTransaction {
            dao.deleteContacts()
            dao.deleteCoPrisoners()
            dao.addOrUpdateCoprisoners(coPrisonerEntities)
            dao.addOrUpdateContacts(contactEntities)
        }
    }
}