package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import by.zenkevich_churun.findcell.entity.Cell
import by.zenkevich_churun.findcell.entity.Schedule
import by.zenkevich_churun.findcell.entity.SchedulePeriod
import java.util.*


/** Implementation of [Schedule] obtained from [ScheduleModel]. **/
internal class UiSchedule private constructor(
    override val arestId: Int,
    override val start: Calendar,
    override val end: Calendar,
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

            val mapCellToPeriod = hashMapOf<Int, SchedulePeriod>()
            val today = model.start.clone() as Calendar

            for(dayCells in model.days) {
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

                for(cellIndex in model.cells.indices) {
                    if(!dayCells.contains(cellIndex)) {
                        // If there was a Period with this Cell, it's over.
                        mapCellToPeriod.remove(cellIndex)
                    }
                }

                today.add(Calendar.DATE, 1)
            }

            return periods
        }
    }
}