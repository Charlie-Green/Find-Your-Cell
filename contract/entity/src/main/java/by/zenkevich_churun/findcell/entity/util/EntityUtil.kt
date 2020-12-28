package by.zenkevich_churun.findcell.entity.util

import java.util.Calendar


internal object EntityUtil {

    fun setToMidnight(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }
}