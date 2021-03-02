package by.zenkevich_churun.findcell.prisoner.repo.jail

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.domain.entity.Cell
import by.zenkevich_churun.findcell.domain.entity.Jail
import by.zenkevich_churun.findcell.prisoner.db.JailsDatabase
import by.zenkevich_churun.findcell.prisoner.db.dao.JailsDao
import by.zenkevich_churun.findcell.prisoner.db.entity.CellEntity
import by.zenkevich_churun.findcell.prisoner.db.entity.JailEntity
import java.io.IOException


internal object JailsRepositoryInternal {

    fun fetchJailsList(
        api: JailsApi,
        dao: JailsDao
    ): List<Jail>? {

        val fetched = try {
            api.jailsList()
        } catch(exc: IOException) {
            Log.w(JailsRepository.LOGTAG, "Failed to fetch jails")
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
            Log.e(JailsRepository.LOGTAG, "Failed to fetch Cell: ${exc.javaClass.name}: ${exc.message}")
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