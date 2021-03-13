package by.sviazen.domain.util

import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit


object CalendarUtil {

    private val tempCal = createCalendar()


    fun date(
        year: Int,
        month: Int,
        day: Int
    ): Long {
        synchronized(tempCal) {
            tempCal.set(year, month, day, 0, 0, 0)
            tempCal[Calendar.MILLISECOND] = 0
            return tempCal.timeInMillis
        }
    }

    fun midnight(time: Long): Long {
        synchronized(tempCal) {
            tempCal.timeInMillis = time

            tempCal.set(Calendar.HOUR_OF_DAY, 0)
            tempCal.set(Calendar.MINUTE, 0)
            tempCal.set(Calendar.SECOND, 0)
            tempCal.set(Calendar.MILLISECOND, 0)

            return tempCal.timeInMillis
        }
    }

    fun daysDifference(
        start: Long,
        end: Long
    ): Int = TimeUnit.DAYS
        .convert(end - start, TimeUnit.MILLISECONDS)
        .toInt()

    fun addDays(time: Long, days: Int): Long {
        synchronized(tempCal) {
            tempCal.timeInMillis = time
            tempCal.add(Calendar.DATE, days)
            return tempCal.timeInMillis
        }
    }


    private fun createCalendar(): Calendar {
        val timeZone = TimeZone.getTimeZone("UTC")
        return Calendar.getInstance(timeZone)
    }
}