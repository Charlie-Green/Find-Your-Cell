package by.zenkevich_churun.findcell.domain.contract.sched

import by.zenkevich_churun.findcell.domain.entity.Schedule
import com.google.gson.annotations.SerializedName


/** client -> server to update [SchedulePeriod]s. **/
class UpdatedSchedulePojo(

    @SerializedName("arest")
    var arestId: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    @SerializedName("periods")
    var periods: List<SchedulePeriodPojo> ) {


    companion object {

        fun from(
            schedule: Schedule,
            passwordBase64: String
        ): UpdatedSchedulePojo {

            val periodPojos = schedule.periods.map { p ->
                SchedulePeriodPojo.from(p)
            }

            return UpdatedSchedulePojo(
                schedule.arestId,
                passwordBase64,
                periodPojos
            )
        }
    }
}