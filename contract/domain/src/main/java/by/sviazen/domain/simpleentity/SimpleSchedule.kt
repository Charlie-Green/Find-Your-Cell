package by.sviazen.domain.simpleentity

import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Schedule
import by.sviazen.domain.entity.SchedulePeriod
import by.sviazen.domain.util.EntityUtil


class SimpleSchedule(
    override val arestId: Int,
    startMillis: Long,
    endMillis: Long,
    override val cells: List<Cell>,
    override val periods: List<SchedulePeriod>
): Schedule() {

    override val start: Long
    override val end: Long


    init {
        start = EntityUtil.midnight(startMillis)
        end = EntityUtil.midnight(endMillis)
    }
}