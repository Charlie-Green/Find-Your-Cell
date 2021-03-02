package by.zenkevich_churun.findcell.server.internal.repo.cellentry

import by.zenkevich_churun.findcell.domain.contract.cellentry.CellEntryPojo
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.scell.ScheduleCellsDao
import by.zenkevich_churun.findcell.server.internal.dao.speriod.SchedulePeriodsDao
import by.zenkevich_churun.findcell.server.internal.entity.key.ScheduleCellEntryKey
import by.zenkevich_churun.findcell.server.internal.entity.table.ScheduleCellEntryEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException


class CellEntriesRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var arestsDao: ArestsDao

    @Autowired
    private lateinit var periodsDao: SchedulePeriodsDao

    @Autowired
    private lateinit var cellsDao: ScheduleCellsDao


    fun add(
        cell: CellEntryPojo,
        passwordHash: ByteArray ) {

        validateByArestId(cell.arestId, passwordHash)

        val entity = ScheduleCellEntryEntity()
        entity.key = ScheduleCellEntryKey(
            cell.arestId,
            cell.jailId,
            cell.cellNumber
        )

        cellsDao.save(entity)
    }


    fun update(
        passwordHash: ByteArray,
        arestId: Int,
        oldJailId: Int, oldCellNumber: Short,
        newJailId: Int, newCellNumber: Short ) {

        // Validate user credentials:
        validateByArestId(arestId, passwordHash)

        // Insert the new Cell:
        val newCell = ScheduleCellEntryEntity()
        newCell.key = ScheduleCellEntryKey(arestId, newJailId, newCellNumber)
        cellsDao.save(newCell)

        // Replace references:
        periodsDao.replaceCellReferences(
            arestId,
            oldJailId, oldCellNumber,
            newJailId, newCellNumber
        )

        // Now delete the oldCell without concern about the foreign key constraint:
        deleteCell(arestId, oldJailId, oldCellNumber)
    }


    fun delete(data: CellEntryPojo, passwordHash: ByteArray) {

        // Validate credentials:
        validateByArestId(data.arestId, passwordHash)

        // Delete all Periods referencing this ScheduleCellEntry:
        periodsDao.deleteForCell(
            data.arestId,
            data.jailId,
            data.cellNumber
        )

        // Delete the ScheduleCellEntry itself:
        deleteCell(data.arestId, data.jailId, data.cellNumber)
    }


    private fun validateByArestId(
        arestId: Int,
        passwordHash: ByteArray ) {

        val prisonerId = arestsDao.prisonerId(arestId)
        validateCredentials(prisonerId, passwordHash)
    }

    private fun deleteCell(
        arestId: Int,
        jailId: Int,
        cellNumber: Short ) {

        val cellKey = ScheduleCellEntryKey(arestId, jailId, cellNumber)
        try {
            cellsDao.deleteById(cellKey)
        } catch(exc: EmptyResultDataAccessException) {
            throw IllegalArgumentException(
                "CellEntry($arestId, $jailId, $cellNumber) doesn't exist",
                exc
            )
        }
    }
}