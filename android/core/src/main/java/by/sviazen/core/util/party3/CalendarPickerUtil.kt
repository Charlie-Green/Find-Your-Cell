package by.sviazen.core.util.party3

import com.savvi.rangedatepicker.CalendarPickerView


object CalendarPickerUtil {

    fun selectedRange(cal: CalendarPickerView): Pair<Long, Long>? {
            val dates = cal.selectedDates
            if(dates.isEmpty()) {
                return null
            }

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

            return Pair(start, end)
        }
}