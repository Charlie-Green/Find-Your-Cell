package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import by.zenkevich_churun.findcell.core.entity.sched.SchedulePeriod
import java.util.*


class SchedulePeriodModel(
    override val startDate: Calendar,
    override val endDate: Calendar,
    override val cellIndex: Int
): SchedulePeriod() {

    init {
        // For easier comparasion:
        ScheduleModelUtil.setToMidnight(startDate)
        ScheduleModelUtil.setToMidnight(endDate)
    }


    fun split(day: Calendar): Pair<SchedulePeriodModel, SchedulePeriodModel> {
        if(day.before(startDate) || day.after(endDate)) {
            throw IllegalArgumentException("Day doesn't belong to period")
        }

        return Pair(
            SchedulePeriodModel(
                startDate,
                day.clone() as Calendar,
                cellIndex
            ),
            SchedulePeriodModel(
                day.clone() as Calendar,
                endDate,
                cellIndex
            )
        )
    }
}