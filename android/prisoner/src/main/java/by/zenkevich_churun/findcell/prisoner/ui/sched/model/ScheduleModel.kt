package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod
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


    fun markDayWithCell(cellIndex: Int, day: Calendar) {
        ScheduleModelUtil.setToMidnight(day)
        days[ dayIndex(day) ].add(cellIndex)
    }

    fun dayAt(index: Int): ScheduleDayModel {
        val day = dayWithIndex(index)
        val dayData = days[index]

        return ScheduleDayModel(
            day,
            cellData(day, dayData),
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

            today.add(Calendar.DATE, 1)
        }

        return periods
    }

    private fun dayIndex(day: Calendar): Int {
        val differenceMillis = day.timeInMillis - start.timeInMillis
        return (differenceMillis / MILLIS_PER_DAY).toInt()
    }

    private fun dayWithIndex(index: Int): Calendar {
        val cal = start.clone() as Calendar
        cal.add(Calendar.DATE, index)
        return cal
    }


    private fun cellData(day: Calendar, dayData: HashSet<Int>): String {
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

            sb.append( cells[cellIndex].jailName )
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
        private const val MILLIS_PER_DAY = 86_400_000L

        private val dateFormat = SimpleDateFormat("dd.MM.YYYY")
    }
}