package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.serial.sched.pojo.LightSchedulePojo
import by.zenkevich_churun.findcell.serial.sched.pojo.PeriodPojo
import com.google.gson.annotations.SerializedName


open class LightSchedulePojo1: LightSchedulePojo {

    @SerializedName("id")
    override var arestId: Int? = null

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("periods")
    var periodPojos: List<PeriodPojo1> = listOf()


    override val periods: List<PeriodPojo>
        get() = periodPojos


    companion object {

        fun from(
            schedule: Schedule,
            passwordBase64: String
        ): LightSchedulePojo1 {

            val pojo = LightSchedulePojo1()
            pojo.arestId = schedule.arestId
            pojo.passwordBase64 = passwordBase64
            pojo.periodPojos = schedule.periods.map { period ->
                PeriodPojo1.from(period)
            }

            return pojo
        }
    }
}