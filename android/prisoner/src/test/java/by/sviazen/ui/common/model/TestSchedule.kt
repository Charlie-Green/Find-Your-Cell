package by.sviazen.prisoner.ui.common.model

import by.sviazen.core.util.std.CalendarUtil
import by.sviazen.entity.entity.Cell
import by.sviazen.entity.entity.Schedule
import by.sviazen.entity.entity.SchedulePeriod
import java.util.*


internal class TestSchedule(
    override val arestId: Int,
    override val start: Calendar,
    override val end: Calendar,
    override val cells: List<Cell>,
    override val periods: List<SchedulePeriod>
): Schedule() {

    init {
        CalendarUtil.setToMidnight(start)
        CalendarUtil.setToMidnight(end)
    }
}