package by.sviazen.core.util.party3

import by.sviazen.domain.util.CalendarUtil
import com.savvi.rangedatepicker.CalendarPickerView
import java.util.*


object CalendarPickerUtil {

    fun selectedRange(cal: CalendarPickerView): Pair<Long, Long>? {
        val dates = cal.selectedDates
        if(dates.isEmpty()) {
            return null
        }

        // Get start and end as the minimum and maximum
        var start = Long.MAX_VALUE
        var end   = Long.MIN_VALUE
        for(date in dates) {
            val millis = date.time
            if(millis < start) {
                start = millis
            }
            if(millis > end) {
                end = millis
            }
        }

        // CalendarPickerView returns time in TimeZone.default.
        // But we need same dates but in UTC, so add the difference:
        val defaultZone = TimeZone.getDefault()
        CalendarUtil.midnight(start)
        return Pair(
            start + defaultZone.getOffset(start),
            end + defaultZone.getOffset(end)
        )
    }
}