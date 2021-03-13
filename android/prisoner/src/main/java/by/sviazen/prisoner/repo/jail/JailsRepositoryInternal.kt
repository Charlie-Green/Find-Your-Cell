package by.sviazen.prisoner.repo.jail

import android.content.Context
import android.util.Log
import by.sviazen.core.api.jail.JailsApi
import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Jail
import by.sviazen.prisoner.db.JailsDatabase
import by.sviazen.prisoner.db.dao.JailsDao
import by.sviazen.prisoner.db.entity.CellEntity
import by.sviazen.prisoner.db.entity.JailEntity
import java.io.IOException


internal object JailsRepositoryInternal {

    val LOGTAG = "FindCell-Jails"


    fun fetchJailsList(
        api: JailsApi,
        dao: JailsDao
    ): List<Jail>? {

        val fetched = try {
            api.jailsList()
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to fetch jails")
            return null
        }

        val jailEntities = fetched.map { jail ->
            JailEntity.from(jail)
        }
        dao.addOrUpdate(jailEntities)

        return fetched
    }

    fun jailName(
        dao: JailsDao,
        api: JailsApi,
        id: Int
    ): String? {

        val name = dao.jailName(id)
        if(name != null) {
            return name
        }

        val jails = fetchJailsList(api, dao) ?: return null

        val jail = jails.find { j ->
            j.id == id
        }
        return jail?.name
    }


    fun cell(
        appContext: Context,
        api: JailsApi,
        jailId: Int,
        jailName: String,
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

        val cells = try {
            api.cells(jailId, jailName)
        } catch(exc: IOException) {
            Log.e(LOGTAG, "Failed to fetch Cell: ${exc.javaClass.name}: ${exc.message}")
            return null
        }

        val entities = cells.map { c ->
            CellEntity.from(jailId, c)
        }
        dao.addOrUpdate(entities)

        return cells.find { c ->
            c.number == cellNumber
        }
    }
}