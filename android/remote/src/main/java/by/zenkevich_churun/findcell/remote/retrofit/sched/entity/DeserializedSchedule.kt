package by.zenkevich_churun.findcell.remote.retrofit.sched.entity

import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.core.api.sched.SchedulePropertiesAccessor
import by.zenkevich_churun.findcell.serial.sched.pojo.CellPojo
import by.zenkevich_churun.findcell.serial.sched.pojo.PeriodPojo
import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import java.util.Calendar


internal class DeserializedSchedule
private constructor(): Schedule() {

    override var arestId: Int = 0
    override var start: Long = 0L
    override var end: Long = 0L
    override lateinit var cells: List<DeserializedCell>
    override lateinit var periods: List<DeserializedPeriod>


    companion object {

        fun from(
            pojo: SchedulePojo,
            props: SchedulePropertiesAccessor,
        ): DeserializedSchedule {

            val sched = DeserializedSchedule()

            sched.arestId = pojo.arestId
                ?: throw KotlinNullPointerException("Mapping requires arestId")
            sched.start   = pojo.start
            sched.end     = pojo.end
            sched.cells   = deserializedCells(pojo.cells, props)
            sched.periods = deserializedPeriods(pojo.periods)

            return sched
        }

        private fun deserializedCells(
            cells: List<CellPojo>,
            props: SchedulePropertiesAccessor
        ): List<DeserializedCell> {

            return cells.map { c ->
                DeserializedCell.from(c, props)
            }
        }

        private fun deserializedPeriods(
            periods: List<PeriodPojo>,
        ): List<DeserializedPeriod> {

            return periods.map { p ->
                DeserializedPeriod.from(p)
            }
        }
    }
}