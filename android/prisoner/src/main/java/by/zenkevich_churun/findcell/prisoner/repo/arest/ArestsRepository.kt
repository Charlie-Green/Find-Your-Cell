package by.zenkevich_churun.findcell.prisoner.repo.arest

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.core.entity.arest.Arest
import by.zenkevich_churun.findcell.core.entity.arest.LightArest
import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.prisoner.db.JailsDatabase
import by.zenkevich_churun.findcell.prisoner.db.entity.JailEntity
import by.zenkevich_churun.findcell.prisoner.repo.common.PrisonerStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArestsRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val arestsApi: ArestsApi,
    private val jailsApi: JailsApi,
    private val prisonerStore: PrisonerStorage ) {


    fun arestsList(): GetArestsResult {
        val prisoner = prisonerStore.prisonerLD.value
            ?: return GetArestsResult.NotAuthorized
        val jailsResult = jailsList(true)
        val jails = jailsResult.jails ?: return GetArestsResult.NetworkError

        val lightArests = try {
            arestsApi.get(prisoner.id, prisoner.passwordHash)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to fetched arests list: ${exc.javaClass.name}: ${exc.message}")
            return GetArestsResult.NetworkError
        }

        val arests = mapArests(lightArests, jails, jailsResult.cached)
        return GetArestsResult.Success(arests)
    }


    private fun jailsList(
        checkCache: Boolean
    ): JailsListResult {

        val dao = JailsDatabase.get(appContext).jailsDao

        if(checkCache) {
            val cached = dao.jails()
            if(!cached.isEmpty()) {
                return JailsListResult(cached, true)
            }
        }

        val fetched = try {
            jailsApi.jailsList()
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to fetche jails list: ${exc.javaClass.name}: ${exc.message}")
            return JailsListResult(null, false)
        }

        val entities = fetched.map { jail ->
            JailEntity.from(jail)
        }
        dao.addOrUpdate(entities)

        return JailsListResult(entities, false)
    }

    private fun mapArests(
        lightArests: List<LightArest>,
        jailsList: List<Jail>,
        jailsCached: Boolean
    ): List<Arest> {

        val arests = mutableListOf<Arest>()
        var jails = jailsList
        var cached = jailsCached

        for(la in lightArests) {

            var arest = Arest.from(la, jails)

            if(arest == null && cached) {
                Log.w(LOGTAG, "Missing a Jail for arest ID ${la.id}. Trying to update cache.")

                // Some Jail is missing in the cache.
                // Try to update the cache (FORCE a network call now):
                jails = jailsList(false).jails ?: jails
                cached = false

                // Cache is updated (or just simply can't be updated).
                // Try once again:
                arest = Arest.from(la, jails)
            }

            if(arest == null) {
                // The jails list is already up-to-date, or can't be updated,
                // or updating it didn't change anything. Skip this arest.
                Log.w(LOGTAG, "Skip arest of ID ${la.id}: missing a Jail.")
                continue
            }

            arests.add(arest)
        }

        return arests
    }


    companion object {
        private const val LOGTAG = "FindCell-Arests"
    }


    private class JailsListResult(
        val jails: List<Jail>?,
        val cached: Boolean
    )
}