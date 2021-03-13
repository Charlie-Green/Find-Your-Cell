package by.sviazen.prisoner.ui.common.sched.util

import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Schedule
import by.sviazen.domain.entity.SchedulePeriod
import by.sviazen.domain.simpleentity.SimpleSchedule
import by.sviazen.domain.util.CalendarUtil
import by.sviazen.prisoner.ui.common.sched.cell.CellModel
import by.sviazen.prisoner.ui.common.sched.period.ScheduleModel
import by.sviazen.prisoner.ui.common.sched.period.SchedulePeriodModel


/** Implementation of [Schedule] obtained from [ScheduleModel]. **/
internal class ScheduleMapper(
    private val colorGen: ScheduleModelColorGenerator
) {

    // =================================================================================
    // Public:

    fun fromSchedule(schedule: Schedule): ScheduleModel {
        colorGen.reset()
        val dayCount = CalendarUtil.daysDifference(schedule.start, schedule.end) + 1

        return ScheduleModel(
            schedule.arestId,
            schedule.start,
            schedule.end,
            cellModels(schedule.cells),
            days(schedule.start, dayCount, schedule.periods)
        )
    }

    fun toSchedule(model: ScheduleModel): Schedule {
        return SimpleSchedule(
            model.arestId,
            model.start,
            model.end,
            model.cells,
            resolvePeriods(model)
        )
    }


    // =================================================================================
    // Private (fromSchedule):

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
                colorGen.currentNumberBackColor,
                colorGen.currentTextColor
            )

            models.add(model)
        }

        return models
    }

    private fun days(
        start: Long,
        dayCount: Int,
        periods: List<SchedulePeriod>
    ): Array< HashSet<Int> > {

        val days = Array< HashSet<Int> >(dayCount) { index ->
            hashSetOf()
        }

        for(period in periods) {
            val startIndex = CalendarUtil.daysDifference(start, period.start)
            val endIndex = CalendarUtil.daysDifference(start, period.end)

            for(dayIndex in startIndex..endIndex) {
                days[dayIndex].add(period.cellIndex)
            }
        }

        return days
    }


    // =================================================================================
    // Private (toSchedule)

    private fun resolvePeriods(model: ScheduleModel): List<SchedulePeriod> {
        val periods = mutableListOf<SchedulePeriod>()

        val mapCellToPeriod = hashMapOf<Int, SchedulePeriodModel>()
        var today = model.start

        for(dayCells in model.days) {
            for(cellIndex in dayCells) {
                val period = mapCellToPeriod[cellIndex]
                if(period == null) {
                    val newPeriod = SchedulePeriodModel(
                        today,
                        today,
                        cellIndex
                    )

                    periods.add(newPeriod)
                    mapCellToPeriod[cellIndex] = newPeriod
                } else {
                    period.end = CalendarUtil.addDays(period.end, 1)
                }
            }

            for(cellIndex in model.cells.indices) {
                if(!dayCells.contains(cellIndex)) {
                    // If there was a Period with this Cell, it's over.
                    mapCellToPeriod.remove(cellIndex)
                }
            }

            today = CalendarUtil.addDays(today, 1)
        }

        return periods
    }
}