package by.sviazen.prisoner.ui.common.sched.period

import by.sviazen.core.util.std.CalendarUtil
import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Jail
import by.sviazen.domain.entity.Schedule
import by.sviazen.domain.entity.SchedulePeriod
import by.sviazen.prisoner.ui.common.sched.cell.CellModel
import by.sviazen.prisoner.ui.common.sched.util.ScheduleMapper
import by.sviazen.prisoner.ui.common.sched.util.ScheduleModelColorGenerator
import kotlin.collections.HashSet


/** Corresponds to [Schedule] displayed and edited on UI.
  * The class cannot be an implementation of [Schedule] itself
  * because of different [SchedulePeriod]s storage paradigms:
  * [Schedule] stores a list of [SchedulePeriod]s, where as [ScheduleModel]
  * stores the data on day-to-day basis.
  * However, this class can be mapped to [Schedule].
  * @see UiSchedule **/
class ScheduleModel(
    val arestId: Int,
    startTime: Long,
    endTime: Long,
    val cells: MutableList<CellModel>,

    /** Element j corresponds to the day start+j.
      * The element is the set of [Cell] indices for all [Cell]s
      * the user was kept in during this day. **/
    internal val days: Array< HashSet<Int> > ) {

    val start = CalendarUtil.midnight(startTime)
    val end = CalendarUtil.midnight(endTime)


    val dayCount: Int
        get() = CalendarUtil.daysDifference(start, end) + 1

    fun markDayWithCell(cellIndex: Int, dayTime: Long) {
        val day = CalendarUtil.midnight(dayTime)

        val cellIndices = days[ dayIndex(day) ]
        if(cellIndices.contains(cellIndex)) {
            cellIndices.remove(cellIndex)
        } else {
            cellIndices.add(cellIndex)
        }
    }

    fun dayAt(index: Int): ScheduleDayModel {
        val day = dayWithIndex(index)
        val cellIndices = days[index]

        return ScheduleDayModel(
            day,
            dayData(cellIndices),
            textColor(cellIndices),
            backColors(cellIndices)
        )
    }

    fun addCell(jail: Jail, cellNumber: Short, seats: Short) {
        val existingCell = cells.find { cell ->
            cell.jailId == jail.id && cell.number == cellNumber
        }
        if(existingCell != null) {
            return
        }

        val backColor = colorGen.next
        val newCell = CellModel(
            jail.id,
            jail.name,
            cellNumber,
            seats,
            backColor,
            colorGen.currentNumberBackColor,
            colorGen.currentTextColor
        )

        cells.add(newCell)
    }

    fun updateCell(
        oldJailId: Int, oldCellNumber: Short,
        newJail: Jail, newCellNumber: Short,
        newSeats: Short ) {

        val cellIndex = cells.indexOfFirst { cell ->
            cell.jailId == oldJailId && cell.number == oldCellNumber
        }

        if(cellIndex in cells.indices) {
            val oldCell = cells[cellIndex]
            cells[cellIndex] = CellModel(
                newJail.id,
                newJail.name,
                newCellNumber,
                newSeats,
                oldCell.backColor,
                oldCell.numberBackColor,
                oldCell.textColor
            )
        }
    }

    fun deleteCell(jailId: Int, cellNumber: Short) {
        val pivotIndex = cells.indexOfFirst { cell ->
            cell.jailId == jailId && cell.number == cellNumber
        }
        if(pivotIndex !in cells.indices) {
            return
        }

        // Remove the cell.
        cells.removeAt(pivotIndex)

        // Update cell indices:
        for(j in days.indices) {
            days[j] = updateCellIndices(days[j], pivotIndex)
        }
    }




    private fun dayIndex(day: Long): Int {
        return CalendarUtil.daysDifference(start, day)
    }

    private fun dayWithIndex(index: Int): Long {
        return CalendarUtil.addDays(start, index)
    }


    private fun updateCellIndices(
        oldIndices: HashSet<Int>,
        pivotIndex: Int
    ): HashSet<Int> {

        val newIndices = hashSetOf<Int>()

        for(oldIndex in oldIndices) {
            when {
                oldIndex < pivotIndex -> {
                    // The Cell remains at the same index.
                    newIndices.add(oldIndex)
                }

                oldIndex > pivotIndex -> {
                    // The Cell moves 1 position back:
                    newIndices.add(oldIndex - 1)
                }

                // Otherwise, the Cell is removed.
            }
        }

        return newIndices
    }


    fun toSchedule(): Schedule
        = scheduleMapper.toSchedule(this)


    private fun dayData(cellIndices: HashSet<Int>): String {
        val sb = StringBuilder(16*cellIndices.size)

        for(cellIndex in cellIndices) {
            if(!sb.isEmpty()) {
                sb.append(". ")
            }

            sb.append( cells[cellIndex].toString() )
        }

        return sb.toString()
    }

    private fun textColor(dayData: HashSet<Int>): Int {
        return if(dayData.isEmpty()) ScheduleDayModel.UNDEFINED_COLOR
            else cells[dayData.first()].textColor
    }

    private fun backColors(dayData: HashSet<Int>): List<Int> {
        return dayData.map { cellIndex ->
            cells[cellIndex].backColor
        }
    }


    companion object {
        private val colorGen = ScheduleModelColorGenerator()
        private val scheduleMapper = ScheduleMapper(colorGen)

        fun from(schedule: Schedule): ScheduleModel
            = scheduleMapper.fromSchedule(schedule)
    }
}