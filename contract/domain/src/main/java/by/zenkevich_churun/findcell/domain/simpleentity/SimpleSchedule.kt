package by.zenkevich_churun.findcell.domain.simpleentity

import by.zenkevich_churun.findcell.domain.entity.Cell
import by.zenkevich_churun.findcell.domain.entity.Schedule
import by.zenkevich_churun.findcell.domain.entity.SchedulePeriod


class SimpleSchedule(
    override val arestId: Int,
    override val start: Long,
    override val end: Long,
    override val cells: List<Cell>,
    override val periods: List<SchedulePeriod>
): Schedule()