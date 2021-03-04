package by.sviazen.prisoner.db.cache

import android.content.Context
import by.sviazen.core.injected.db.JailsCache
import by.sviazen.domain.contract.jail.FullJailPojo
import by.sviazen.prisoner.db.JailsDatabase
import by.sviazen.prisoner.db.entity.CellEntity
import by.sviazen.prisoner.db.entity.JailEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class JailsCacheImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
): JailsCache {

    override fun cache(jails: List<FullJailPojo>) {
        val db = JailsDatabase.get(appContext)
        val dao = db.jailsDao

        val jailEntities = jails.map { j ->
            JailEntity.from(j)
        }

        dao.addOrUpdate(jailEntities)

        val cells = mutableListOf<CellEntity>()
        for(j in jails) {

            for(index in j.seatCounts.indices) {
                val seatCount = j.seatCounts[index]
                if(seatCount < 0) {
                    // Cell with this number doesn't exist.
                    continue
                }

                val cell = CellEntity(
                    j.id,
                    (index + 1).toShort(),
                    seatCount
                )

                cells.add(cell)
            }
        }

        db.runInTransaction {
            dao.deleteCells()             // Delete the old Cells.
            dao.addOrUpdateCells(cells)   // Replace with the new ones.
        }

    }
}