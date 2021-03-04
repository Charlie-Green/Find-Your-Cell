package by.sviazen.prisoner.repo.arest

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import by.sviazen.core.api.arest.ArestsApi
import by.sviazen.core.api.jail.JailsApi
import by.sviazen.prisoner.db.JailsDatabase
import by.sviazen.prisoner.db.entity.JailEntity
import by.sviazen.core.common.prisoner.PrisonerStorage
import by.sviazen.core.injected.sync.AutomaticSyncManager
import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Jail
import by.sviazen.domain.contract.arest.CreateOrUpdateArestResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArestsRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val arestsApi: ArestsApi,
    private val jailsApi: JailsApi,
    private val cache: ArestsCache,
    private val prisonerStore: PrisonerStorage,
    private val autoSyncMan: AutomaticSyncManager ) {

    val arestsLD: LiveData< List<Arest> >
        get() = cache.arestsLD


    /** In case of success, the new value is emitted by [arestsLD] **/
    fun arestsList(): GetArestsResult {
        val prisoner = prisonerStore.prisonerLD.value
            ?: return GetArestsResult.NotAuthorized

        var jailsResult = jailsList(true)
        var jails = jailsResult.jails ?: return GetArestsResult.NetworkError

        val lightArests = try {
            arestsApi.get(prisoner.id, prisoner.passwordHash!!)
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
        cache.submit(arests)
        return GetArestsResult.Success
    }


    /** @return a pair of (response; int) where:
      *         - response notifies if the call succeeded;
      *         - int is list position of the newly created [Arest],
      *                 or any integer if the call failed., **/
    fun addArest(
        start: Long,
        end: Long
    ): Pair<CreateOrUpdateArestResponse, Int> {

        val prisoner = prisonerStore.prisonerLD.value
            ?: throw IllegalStateException("Not authorized")

        val response = try {
            arestsApi.create(
                prisoner.id,
                prisoner.passwordHash!!,
                start,
                end
            )
        } catch(exc: IOException) {
            return Pair(CreateOrUpdateArestResponse.NetworkError, -1)
        }

        if(response !is CreateOrUpdateArestResponse.Success) {
            return Pair(response, -1)
        }

        val arest = Arest(
            response.arestId,
            start,
            end,
            listOf()  // Jails list is empty because the Arest was just created.
        )
        val position = cache.insert(arest)

        return Pair(response, position)
    }

    /** @return [Collection] of list positions [Arest]s were deleted from,
      *         or null if deletion failed. **/
    fun deleteArests(ids: Collection<Int>): List<Int>? {
        val prisoner = prisonerStore.prisonerLD.value ?: return null

        try {
            arestsApi.delete(prisoner.id, prisoner.passwordHash!!, ids)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to delete arests: ${exc.message}")
            return null
        }

        // Deletion of Arest may stop some CoPrisoners from being suggested:
        autoSyncMan.clearCoPrisonersCache()
        autoSyncMan.set(true)

        return cache.delete(ids.toHashSet())
    }

    /** Clears current value of [arestsLD] **/
    fun clearArests() {
        cache.clear()
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