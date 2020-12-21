package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import androidx.core.graphics.ColorUtils
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod
import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import java.util.Calendar
import kotlin.collections.HashSet


class ScheduleModel private constructor(
    val arestId: Int,
    val start: Calendar,
    val end: Calendar,
    val cells: MutableList<CellModel>,

    /** Element j corresponds to the day start+j.
      * The element is the set of [Cell] indices for all [Cell]s
      * the user was kept in during this day. **/
    private val days: Array< HashSet<Int> > ) {


    init {
        CalendarUtil.setToMidnight(start)
        CalendarUtil.setToMidnight(end)
    }


    val dayCount: Int
        get() = CalendarUtil.daysDifference(start, end) + 1

    fun markDayWithCell(cellIndex: Int, day: Calendar) {
        CalendarUtil.setToMidnight(day)

        val cellIndices = days[ dayIndex(day) ]
        if(cellIndices.contains(cellIndex)) {
            cellIndices.remove(cellIndex)
        } else {
            cellIndices.add(cellIndex)
        }
    }

    fun dayAt(index: Int): ScheduleDayModel {
        val day = dayWithIndex(index)
        val dayData = days[index]

        return ScheduleDayModel(
            day,
            dayData(day, dayData),
            textColor(dayData),
            backColors(dayData)
        )
    }

    fun toSchedule(): Schedule {
        return Schedule(
            arestId,
            start,
            end,
            cells,
            resolvePeriods()
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
            numberBackColor(backColor),
            textColor(backColor)
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


    private fun resolvePeriods(): List<SchedulePeriod> {
        val periods = mutableListOf<SchedulePeriod>()

        val mapCellToPeriod = hashMapOf<Int, SchedulePeriod>()
        val today = start.clone() as Calendar

        for(dayCells in days) {
            for(cellIndex in dayCells) {
                val period = mapCellToPeriod[cellIndex]
                if(period == null) {
                    val newPeriod = SchedulePeriodModel(
                        today.clone() as Calendar,
                        today.clone() as Calendar,
                        cellIndex
                    )

                    periods.add(newPeriod)
                    mapCellToPeriod[cellIndex] = newPeriod
                } else {
                    period.endDate.add(Calendar.DATE, 1)
                }
            }

            for(cellIndex in cells.indices) {
                if(!dayCells.contains(cellIndex)) {
                    // If there was a Period with this Cell, it's over.
                    mapCellToPeriod.remove(cellIndex)
                }
            }

            today.add(Calendar.DATE, 1)
        }

        return periods
    }

    private fun dayIndex(day: Calendar): Int {
        return CalendarUtil.daysDifference(start, day)
    }

    private fun dayWithIndex(index: Int): Calendar {
        val cal = start.clone() as Calendar
        cal.add(Calendar.DATE, index)
        return cal
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


    private fun dayData(day: Calendar, dayData: HashSet<Int>): String {
        val sb = StringBuilder(16*dayData.size)

        for(cellIndex in dayData) {
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
            val originalColor = cells[cellIndex].backColor
            ColorUtils.setAlphaComponent(originalColor, 192)
        }
    }


    companion object {
        private val colorGen = ScheduleModuleColorGenerator()


        fun from(schedule: Schedule): ScheduleModel {
            val dayCount = CalendarUtil.daysDifference(schedule.start, schedule.end) + 1
            return ScheduleModel(
                schedule.arestId,
                schedule.start,
                schedule.end,
                cellModels(schedule.cells),
                days(schedule.start, dayCount, schedule.periods)
            )
        }


        private fun cellModels(cells: List<Cell>): MutableList<CellModel> {
            val models = mutableListOf<CellModel>()

            for(cell in cells) {
                val backColor = colorGen.next
                val model = CellModel(
                    cell.jailId,
                    cell.jailName,
                    cell.number,
                    cell.seats,
                    backColor,
                    numberBackColor(backColor),
                    textColor(backColor)
                )

                models.add(model)
            }

            return models
        }

        private fun days(
            startDate: Calendar,
            dayCount: Int,
            periods: List<SchedulePeriod>
        ): Array< HashSet<Int> > {

            val days = Array< HashSet<Int> >(dayCount) { index ->
                hashSetOf()
            }

            for(period in periods) {
                val startIndex = CalendarUtil.daysDifference(startDate, period.startDate)
                val endIndex = CalendarUtil.daysDifference(startDate, period.endDate)

                for(dayIndex in startIndex..endIndex) {
                    days[dayIndex].add(period.cellIndex)
                }
            }

            return days
        }


        private fun numberBackColor(backColor: Int): Int {
            val r0 = (backColor and 0x00_ff0000.toInt()) shr 16
            val g0 = (backColor and 0x00_00ff00.toInt()) shr 8
            val b0 = backColor and 0x00_0000ff.toInt()

            val r = r0/2
            val g = g0/2
            val b = b0/2

            val alpha = backColor and 0xff_000000.toInt()
            return alpha or (r shl 16) or (g shl 8) or b
        }

        private fun textColor(backColor: Int): Int {
            val rgb0 = backColor and 0xffffff.toInt()
            val rgb = 0xffffff - rgb0
            val alpha = backColor and 0xff_000000.toInt()
            return alpha or rgb
        }
    }
}