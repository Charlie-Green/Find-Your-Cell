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
    override var cells: List<CellPojo> = listOf()

    @SerializedName("periods")
    override var periods: List<PeriodPojo> = listOf()
}