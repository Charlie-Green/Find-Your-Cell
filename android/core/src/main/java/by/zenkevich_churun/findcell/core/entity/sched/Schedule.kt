package by.zenkevich_churun.findcell.core.entity.sched

import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import java.util.*


/** A day-to-day schedule of where a [Prisoner] was kept during 1 their arest. **/
class Schedule(
    /** The first day of schedule. **/
    val start: Calendar,

    /** The last day of schedule (the day the [Prisoner] was released). **/
    val end: Calendar,

    /** All [Cell]s the user specified that, during their arest, they have been to.
      * [SchedulePeriod.cellIndex] field points to an element within this list. **/
    val cells: List<Cell>,

    /** [SchedulePeriod]s within this [Schedule].
      * Ideally, they cover the time period of this [Schedule],
      * but they don't have to since, for some days, information
      * about [Prisoner] place of location may be missing. **/
    val periods: List<SchedulePeriod> ) {

    init {
        CalendarUtil.setToMidnight(start)
        CalendarUtil.setToMidnight(end)
    }
}