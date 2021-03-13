package by.sviazen.server.internal.repo.arest

import by.sviazen.domain.util.CalendarUtil
import by.sviazen.server.internal.entity.table.ArestEntity


internal object ArestsUtil {

    fun validate(start: Long, end: Long) {
        if(end < start) {
            throw IllegalArgumentException("Arest ends before it starts")
        }
    }

    fun normalize(a: ArestEntity) {
        a.start = CalendarUtil.midnight(a.start)
        a.end = CalendarUtil.midnight(a.end)
    }
}