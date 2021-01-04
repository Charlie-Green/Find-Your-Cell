package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.serial.sched.pojo.*
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
}