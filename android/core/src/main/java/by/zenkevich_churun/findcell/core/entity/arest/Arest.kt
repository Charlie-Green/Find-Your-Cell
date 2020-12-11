package by.zenkevich_churun.findcell.core.entity.arest

import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import java.util.Calendar


/** Light information about 1 user arest,
  * that is the period between the first and the last day in jails. **/
class Arest(
    /** The first day in a [Jail]. **/
    val start: Calendar,

    /** The last day in a [Jail]. **/
    val end: Calendar,

    /** The full list of [Jail]s the user has been to during this [Arest]. **/
    val jails: List<Jail> ) {

    init {
        CalendarUtil.setToMidnight(start)
        CalendarUtil.setToMidnight(end)
    }
}