package by.zenkevich_churun.findcell.prisoner.repo.arest

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.entity.Jail
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

    private var arests: List<Arest>? = null


    fun arestsList(): GetArestsResult {
        val prisoner = prisonerStore.prisonerLD.value
            ?: return GetArestsResult.NotAuthorized
        var jailsResult = jailsList(true)
        var jails = jailsResult.jails ?: return GetArestsResult.NetworkError

        val lightArests = try {
            arestsApi.get(prisoner.id, prisoner.passwordHash)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to fetched arests list: ${exc.javaClass.name}: ${exc.message}")
            return GetArestsResult.NetworkError
        }

        if( jailsResult.cached &&
            !ArestsMapper.areAllJailsPresent(lightArests, jails) ) {

            // Jails were fetched from cache, but some Jails are missing.
            // Thus, the cache is outdated. Force to fetch Jails from the server.
            jailsResult = jailsList(false)
            jails = jailsResult.jails ?: jails
        }

        val arests = ArestsMapper.map(lightArests, jails)
        ArestsCache.submit(arests)
        return GetArestsResult.Success( ArestsCache.cachedList )
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
            Log.w(LOGTAG, "Failed to fetch jails list: ${exc.javaClass.name}: ${exc.message}")
            return JailsListResult(null, false)
        }

        val entities = fetched.map { jail ->
            JailEntity.from(jail)
        }
        dao.addOrUpdate(entities)

        return JailsListResult(entities, false)
    }


    companion object {
        private const val LOGTAG = "FindCell-Arests"
    }


    private class JailsListResult(
        val jails: List<Jail>?,
        val cached: Boolean
    )
}