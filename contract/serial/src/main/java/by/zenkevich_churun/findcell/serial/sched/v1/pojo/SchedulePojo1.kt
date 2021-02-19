package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.serial.sched.pojo.*
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import com.google.gson.annotations.SerializedName


class SchedulePojo1: SchedulePojo() {

    @SerializedName("id")
    override var arestId: Int? = null

    @SerializedName("start")
    override var start: Long = 0L

    @SerializedName("end")
    override var end: Long = 0L

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("cells")
    var cellPojos: List<CellPojo1> = listOf()

    @SerializedName("periods")
    var periodPojos: List<PeriodPojo1> = listOf()


    override val cells: List<CellPojo>
        get() = cellPojos

    override val periods: List<PeriodPojo>
        get() = periodPojos


    companion object {

        fun from(
            schedule: Schedule,
            passwordHash: ByteArray
        ): SchedulePojo1 {

            val pojo = SchedulePojo1()

            pojo.arestId = schedule.arestId
            pojo.start   = schedule.start.timeInMillis
            pojo.end     = schedule.end.timeInMillis
            pojo.passwordBase64 = Base64Util.encode(passwordHash)
            pojo.periodPojos = schedule.periods.map { p ->
                PeriodPojo1.from(p)
            }

            return pojo
        }
    }
}