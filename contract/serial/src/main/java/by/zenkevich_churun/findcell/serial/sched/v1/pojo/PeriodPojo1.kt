package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import by.zenkevich_churun.findcell.serial.sched.pojo.PeriodPojo
import com.google.gson.annotations.SerializedName


class PeriodPojo1: PeriodPojo() {

    @SerializedName("start")
    override var start: Long = 0L

    @SerializedName("end")
    override var end: Long = 0L

    @SerializedName("cell")
    override var cellIndex: Int = 0


    companion object {

        fun from(p: SchedulePeriod): PeriodPojo1 {
            val pojo = PeriodPojo1()
            pojo.start     = p.startDate.timeInMillis
            pojo.end       = p.endDate.timeInMillis
            pojo.cellIndex = p.cellIndex

            return pojo
        }
    }
}