package by.zenkevich_churun.findcell.prisoner.repo.jail

import android.content.Context
import android.util.Log
import by.zenkevich_churun.findcell.core.api.jail.JailsApi
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
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


    fun cell(
        appContext: Context,
        api: JailsApi,
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
            Log.e(JailsRepository.LOGTAG, "Failed to fetch Cell: ${exc.javaClass.name}: ${exc.message}")
            return null
        }
    }
}