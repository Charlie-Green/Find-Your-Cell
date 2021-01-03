package by.zenkevich_churun.findcell.prisoner.ui.common.model

import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
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