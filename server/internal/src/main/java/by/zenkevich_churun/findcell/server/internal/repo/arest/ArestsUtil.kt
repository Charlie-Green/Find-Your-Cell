package by.zenkevich_churun.findcell.server.internal.repo.arest

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import java.util.Calendar


internal object ArestsUtil {

    private val cal = Calendar.getInstance()


    fun validate(a: LightArest) {
        if(a.end < a.start) {
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