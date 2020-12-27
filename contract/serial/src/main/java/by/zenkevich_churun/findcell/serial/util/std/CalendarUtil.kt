package by.zenkevich_churun.findcell.serial.util.std

import java.util.Calendar


object CalendarUtil {
    private const val MILLIS_PER_DAY = 86_400_000L


    fun setToMidnight(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }

    fun daysDifference(start: Calendar, end: Calendar): Int {
        val diffMillis = end.timeInMillis - start.timeInMillis
        return (diffMillis / MILLIS_PER_DAY).toInt()
    }
}