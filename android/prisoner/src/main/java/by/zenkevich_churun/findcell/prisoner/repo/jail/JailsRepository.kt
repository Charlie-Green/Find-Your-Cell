package by.zenkevich_churun.findcell.prisoner.repo.jail

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.prisoner.db.JailsDatabase
import by.zenkevich_churun.findcell.prisoner.db.entity.CellEntity
import by.zenkevich_churun.findcell.prisoner.db.entity.JailEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JailsRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: JailsApi
) {

    fun jailsList(internet: Boolean): GetJailsResult {
        val dao = JailsDatabase.get(appContext).jailsDao
        val cached = dao.jails()
        if(!cached.isEmpty()) {
            return GetJailsResult.Success(cached)
        }

        if(!internet) {
            return GetJailsResult.FirstTimeNeedInternet
        }

        val fetched = fetchJailsList() ?: return GetJailsResult.FirstTimeError

        val jailEntities = fetched.map { jail ->
            JailEntity.from(jail)
        }
        dao.addOrUpdate(jailEntities)

        return GetJailsResult.Success(fetched)
    }

    fun cell(
        jailId: Int,
        cellNumber: Short,
        internet: Boolean
    ): Cell? {

        val dao = JailsDatabase.get(appContext).cellsDao
        var cell: Cell? = dao.get(jailId, cellNumber)
        if(cell != null) {
            return cell
        }

        if(!internet) {
            return null
        }

        try {
            cell = api.cell(jailId, cellNumber)
            val entity = CellEntity.from(jailId, cell)
            dao.addOrUpdate( listOf(entity) )

            return cell
        } catch(exc: IOException) {
            Log.e(LOGTAG, "Failed to fetch Cell: ${exc.javaClass.name}: ${exc.message}")
            return null
        }
    }


    private fun fetchJailsList(): List<Jail>? {
        try {
            return api.jailsList()
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to fetch jails")
            return null
        }
    }


    companion object {
        private const val LOGTAG = "FindCell-Jails"
    }
}