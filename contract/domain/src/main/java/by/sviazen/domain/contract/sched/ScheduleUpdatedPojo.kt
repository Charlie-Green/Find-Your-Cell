package by.sviazen.domain.contract.sched

import by.sviazen.domain.entity.Schedule
import com.google.gson.annotations.SerializedName


/** client -> server to update [SchedulePeriod]s. **/
class ScheduleUpdatedPojo(

    @SerializedName("arest")
    var arestId: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    @SerializedName("periods")
    var periods: List<SchedulePeriodUpdatedPojo> ) {


    companion object {

        fun from(
            schedule: Schedule,
            passwordBase64: String
        ): ScheduleUpdatedPojo {

            val periodPojos = schedule.periods.map { p ->
                SchedulePeriodUpdatedPojo.from(p, schedule.cells)
            }

            return ScheduleUpdatedPojo(
                schedule.arestId,
                passwordBase64,
                periodPojos
            )
        }
    }
}