package by.zenkevich_churun.findcell.server.internal.repo.cellentry

import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.scell.ScheduleCellsDao
import by.zenkevich_churun.findcell.server.internal.dao.speriod.SchedulePeriodsDao
import by.zenkevich_churun.findcell.server.internal.entity.key.ScheduleCellEntryKey
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class CellEntriesRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var arestsDao: ArestsDao

    @Autowired
    private lateinit var periodsDao: SchedulePeriodsDao

    @Autowired
    private lateinit var cellsDao: ScheduleCellsDao


    fun delete(
        passwordHash: ByteArray,
        arestId: Int,
        jailId: Int,
        cellNumber: Short ) {

        // Validate credentials:
        val prisonerId = arestsDao.prisonerId(arestId)
        validateCredentials(prisonerId, passwordHash)

        // Delete all Periods referencing this ScheduleCellEntry:
        periodsDao.deleteForCell(arestId, jailId, cellNumber)

        // Delete the ScheduleCellEntry itself:
        val cellKey = ScheduleCellEntryKey()
        cellKey.arestId    = arestId
        cellKey.jailId     = jailId
        cellKey.cellNumber = cellNumber
        cellsDao.deleteById(cellKey)
    }
}