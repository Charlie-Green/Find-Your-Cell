package by.zenkevich_churun.findcell.prisoner.ui.common.sched.period

import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import by.zenkevich_churun.findcell.domain.entity.SchedulePeriod


class SchedulePeriodModel(
    startTime: Long,
    endTime: Long,
    override val cellIndex: Int
): SchedulePeriod() {

    override var start: Long = CalendarUtil.midnight(startTime)
    override var end:   Long = CalendarUtil.midnight(endTime)


    fun split(day: Long): Pair<SchedulePeriodModel, SchedulePeriodModel> {
        if(day < start || day > end) {
            throw IllegalArgumentException("Day doesn't belong to period")
        }

        return Pair(
            SchedulePeriodModel(
                start,
                end,
                cellIndex
            ),
            SchedulePeriodModel(
                start,
                end,
                cellIndex
            )
        )
    }
}