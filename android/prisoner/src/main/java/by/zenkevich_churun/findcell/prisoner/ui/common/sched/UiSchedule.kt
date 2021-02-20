package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import java.util.*


/** Implementation of [Schedule] obtained from [ScheduleModel]. **/
internal class UiSchedule private constructor(
    override val arestId: Int,
    override val start: Long,
    override val end: Long,
    override val cells: List<Cell>,
    override val periods: List<SchedulePeriod>
): Schedule() {

    companion object {

        fun from(model: ScheduleModel): UiSchedule {
            return UiSchedule(
                model.arestId,
                model.start,
                model.end,
                model.cells,
                resolvePeriods(model)
            )
        }


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
}