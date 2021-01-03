package by.zenkevich_churun.findcell.core.util.std

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
        if(diffMillis % MILLIS_PER_DAY != 0L) {
            val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS")
            val formattedStart = dateFormat.format(start.time)
            val formattedEnd   = dateFormat.format(end.time)

            throw IllegalArgumentException(
                "Difference between $formattedStart and $formattedEnd " +
                "is not an integer number of days"
            )
        }

        return (diffMillis / MILLIS_PER_DAY).toInt()
    }
}