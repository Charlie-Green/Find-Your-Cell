package by.zenkevich_churun.findcell.server.protocol.controller.sched.map

import by.zenkevich_churun.findcell.entity.entity.*
import by.zenkevich_churun.findcell.serial.sched.pojo.*
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.*
import by.zenkevich_churun.findcell.server.internal.entity.table.ScheduleCellEntryEntity
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.ScheduleView


internal class ScheduleMapper1: ScheduleMapper {

    override fun schedulePojo(view: ScheduleView): SchedulePojo {
        val pojo = SchedulePojo1()

        val cellPojos = view.cellEntries.map { cellEntry ->
            cellPojo(cellEntry)
        }

        val periodPojos = mutableListOf<PeriodPojo>()
        for(periodEntity in view.periodEntities) {

            periodPojo(periodEntity, cellPojos)?.also {
                periodPojos.add(it)
            }
            // In the rare case a Cell for a Period is not found,
            // the Period is skipped.
        }

        pojo.start   = view.arest.start
        pojo.end     = view.arest.end
        pojo.cells   = cellPojos
        pojo.periods = periodPojos

        return pojo
    }


    private fun cellPojo(entry: ScheduleCellEntryEntity): CellPojo {
        val key = entry.key
            ?: throw Error("${ScheduleCellEntryEntity::class.java.simpleName} not initialized")

        return CellPojo1().apply {
            jailId     = key.jailId
            cellNumber = key.cellNumber
        }
    }

    private fun periodPojo(
        entity: PeriodEntity,
        cells: Collection<CellPojo>
    ): PeriodPojo? {

        val key = entity.key
            ?: throw Error("${PeriodEntity::class.java.simpleName} not initialized")

        val pojo = PeriodPojo1().apply {
            start     = key.start
            end       = key.end
            cellIndex = cellIndex(cells, entity.jailId, entity.cellNumber)
        }

        if(pojo.cellIndex !in cells.indices) {
            // This should never happen.
            // If it does, it means the database is polluted.
            warnCellNotFound(entity)
            return null
        }

        return pojo
    }


    private fun cellIndex(
        cells: Collection<CellPojo>,
        jailId: Int,
        cellNumber: Short
    ): Int {

        return cells.indexOfFirst { c ->
            c.jailId == jailId && c.cellNumber == cellNumber
        }
    }

    private fun warnCellNotFound(entity: PeriodEntity) {
        val key = entity.key!!

        val periodClass = SchedulePeriod::class.java.simpleName
        val cellClass   = Cell::class.java.simpleName
        val arestClass  = Arest::class.java.simpleName

        println(
            "ATTENTION!\n" +
                "$periodClass references a $cellClass that is not used in the current $arestClass.\n" +
                "$arestClass: ID=${key.arestId}\n" +
                "$periodClass: start=${key.start}, end=${key.end}\n" +
                "$cellClass: jailId=${entity.jailId}, cellNumber=${entity.cellNumber}"
        )
    }
}