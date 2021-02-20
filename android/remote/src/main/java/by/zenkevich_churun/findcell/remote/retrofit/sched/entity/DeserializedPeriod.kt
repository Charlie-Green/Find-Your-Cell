package by.zenkevich_churun.findcell.remote.retrofit.sched.entity

import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import by.zenkevich_churun.findcell.serial.sched.pojo.PeriodPojo
import java.util.*


internal class DeserializedPeriod
private constructor(): SchedulePeriod() {

    override var start: Long = 0L
    override var end: Long = 0L
    override var cellIndex: Int = -1


    companion object {

        fun from(p: PeriodPojo): DeserializedPeriod {

            val period = DeserializedPeriod()

            period.start = p.start
            period.end = p.end
            period.cellIndex = p.cellIndex

            return period
        }
    }
}