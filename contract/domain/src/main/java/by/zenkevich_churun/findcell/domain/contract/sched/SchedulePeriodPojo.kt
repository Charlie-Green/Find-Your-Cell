package by.zenkevich_churun.findcell.domain.contract.sched

import by.zenkevich_churun.findcell.domain.entity.SchedulePeriod
import com.google.gson.annotations.SerializedName


class SchedulePeriodPojo(

    @SerializedName("cell")
    override var cellIndex: Int,

    @SerializedName("start")
    override val start: Long,

    @SerializedName("end")
    override val end: Long

): SchedulePeriod() {


    constructor(): this(-1, 0L, 0L)
}