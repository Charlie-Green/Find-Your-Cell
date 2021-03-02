package by.zenkevich_churun.findcell.domain.contract.sched

import by.zenkevich_churun.findcell.domain.entity.Schedule
import com.google.gson.annotations.SerializedName


class UpdatedSchedulePojo(

    @SerializedName("arest")
    var arestId: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    cells: List<CellPojo>,

    periods: List<SchedulePeriodPojo>

): GotSchedulePojo(cells, periods) {


    companion object {

        fun from(
            schedule: Schedule,
            passwordBase64: String
        ): UpdatedSchedulePojo {

            val cellPojos = schedule.cells.map { c ->
                CellPojo.from(c)
            }

            val periodPojos = schedule.periods.map { p ->
                SchedulePeriodPojo.from(p)
            }

            return UpdatedSchedulePojo(
                schedule.arestId,
                passwordBase64,
                cellPojos,
                periodPojos
            )
        }
    }
}