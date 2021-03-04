package by.sviazen.server.internal.repo.arest

import by.sviazen.domain.entity.LightArest
import by.sviazen.server.internal.entity.table.ArestEntity
import java.util.Calendar


internal object ArestsUtil {

    private val cal = Calendar.getInstance()


    fun validate(start: Long, end: Long) {
        if(end < start) {
            throw IllegalArgumentException("Arest ends before it starts")
        }
    }

    fun normalize(a: ArestEntity) {
        a.start = setToMidnight(a.start)
        a.end = setToMidnight(a.end)
    }


    private fun setToMidnight(time: Long): Long {
        cal.timeInMillis = time

        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE]      = 0
        cal[Calendar.SECOND]      = 0
        cal[Calendar.MILLISECOND] = 0

        return cal.timeInMillis
    }
}