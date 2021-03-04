package by.sviazen.domain.simpleentity

import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Schedule
import by.sviazen.domain.entity.SchedulePeriod


class SimpleSchedule(
    override val arestId: Int,
    override val start: Long,
    override val end: Long,
    override val cells: List<Cell>,
    override val periods: List<SchedulePeriod>
): Schedule()