package by.sviazen.domain.contract.sched

import by.sviazen.domain.entity.SchedulePeriod
import com.google.gson.annotations.SerializedName


class SchedulePeriodFetchedPojo(

    @SerializedName("cell")
    override var cellIndex: Int,

    @SerializedName("start")
    override val start: Long,

    @SerializedName("end")
    override val end: Long

): SchedulePeriod() {


    constructor(): this(-1, 0L, 0L)


    companion object {

        fun from(
            p: SchedulePeriod
        ) = SchedulePeriodFetchedPojo(p.cellIndex, p.start, p.end)
    }
}