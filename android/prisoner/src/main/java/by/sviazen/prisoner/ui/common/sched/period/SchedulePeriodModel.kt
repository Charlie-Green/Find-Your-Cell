package by.sviazen.prisoner.ui.common.sched.period

import by.sviazen.core.util.std.CalendarUtil
import by.sviazen.domain.entity.SchedulePeriod


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