package by.zenkevich_churun.findcell.serial.sched.pojo

import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import java.util.Calendar


abstract class PeriodPojo: SchedulePeriod() {
    abstract var start: Long
    abstract var end: Long
    abstract override var cellIndex: Int


    override val startDate: Calendar
        get() = Calendar.getInstance().apply { timeInMillis = start }

    override val endDate: Calendar
        get() = Calendar.getInstance().apply { timeInMillis = end }
}