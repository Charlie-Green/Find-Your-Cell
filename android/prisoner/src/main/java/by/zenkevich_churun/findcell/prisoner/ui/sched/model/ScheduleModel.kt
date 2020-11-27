package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod
import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.collections.HashSet


class ScheduleModel private constructor(
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
        days[ dayIndex(day) ].add(cellIndex)
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
            start,
            end,
            cells,
            resolvePeriods()
        )
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


    private fun dayData(day: Calendar, dayData: HashSet<Int>): String {
        val sb = StringBuilder(12 + 16*dayData.size)

        sb.append(dateFormat.format(day.time))
        var isFirstJail = true

        for(cellIndex in dayData) {
            if(isFirstJail) {
                sb.append(": ")
                isFirstJail = false
            } else {
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
        private val dateFormat = SimpleDateFormat("dd.MM.YYYY")
        private val colorGen = ScheduleModuleColorGenerator()


        fun fromSchedule(schedule: Schedule): ScheduleModel {
            val dayCount = CalendarUtil.daysDifference(schedule.start, schedule.end) + 1
            return ScheduleModel(
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
                    cell.jailName,
                    cell.number,
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