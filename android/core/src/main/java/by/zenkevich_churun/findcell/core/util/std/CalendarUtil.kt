package by.zenkevich_churun.findcell.core.util.std

import android.util.Log
import java.util.Date
import java.util.Calendar


object CalendarUtil {
    private const val MILLIS_PER_DAY = 86_400_000L
    private val tempCal = Calendar.getInstance()


    fun midnight(time: Long): Long {
        synchronized(tempCal) {
            tempCal.timeInMillis = time
            setToMidnight(tempCal)
            return tempCal.timeInMillis
        }
    }

    fun setToMidnight(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }


    fun daysDifference(start: Long, end: Long): Int {
        val diffMillis = end - start
        if(diffMillis % MILLIS_PER_DAY != 0L) {
            val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS")
            val formattedStart = dateFormat.format( Date(start) )
            val formattedEnd   = dateFormat.format( Date(end) )

            Log.w(
                CalendarUtil::class.java.simpleName,
                "Difference between $formattedStart and $formattedEnd " +
                "is not an integer number of days"
            )
        }

        var diff2 = 2L*diffMillis / MILLIS_PER_DAY
        if(diff2 % 2L == 1L) {
            ++diff2
        }

        return (diff2 / 2).toInt()
    }


    fun addDays(time: Long, dayCount: Int): Long {
        synchronized(tempCal) {
            tempCal.timeInMillis = time
            tempCal.add(Calendar.DAY_OF_MONTH, dayCount)
            return tempCal.timeInMillis
        }
    }
}