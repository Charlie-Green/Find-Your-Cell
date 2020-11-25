package by.zenkevich_churun.findcell.core.entity.sched

import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import java.util.Calendar


/** Period of time [Prisoner] has been in the same [Cell] for **/
abstract class SchedulePeriod {
    /** The day this period starts. **/
    abstract val startDate: Calendar

    /** The day this [SchedulePeriod] ends
      * (the user was transferred to another cell or released). **/
    abstract val endDate: Calendar

    /** The [Cell] this user lived in,
      * expressed as an index within a list of [Cell]s. **/
    abstract val cellIndex: Int
}