package by.zenkevich_churun.findcell.remote.retrofit.sched.entity

import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import by.zenkevich_churun.findcell.serial.sched.pojo.PeriodPojo
import java.util.*


internal class DeserializedPeriod
private constructor(): SchedulePeriod() {

    override lateinit var startDate: Calendar
    override lateinit var endDate: Calendar
    override var cellIndex: Int = -1


    companion object {

        fun from(p: PeriodPojo): DeserializedPeriod {

            val period = DeserializedPeriod()

            period.startDate = Calendar.getInstance().apply { timeInMillis = p.start }
            period.endDate   = Calendar.getInstance().apply { timeInMillis = p.end }
            period.cellIndex = p.cellIndex

            return period
        }
    }
}