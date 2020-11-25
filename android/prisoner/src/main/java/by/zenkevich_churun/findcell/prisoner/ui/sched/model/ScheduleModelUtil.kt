package by.zenkevich_churun.findcell.prisoner.ui.sched.model

import java.util.Calendar


internal object ScheduleModelUtil {
    fun setToMidnight(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }
}