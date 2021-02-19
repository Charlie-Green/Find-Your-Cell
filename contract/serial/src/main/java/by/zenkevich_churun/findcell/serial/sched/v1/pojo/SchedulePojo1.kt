package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.serial.sched.pojo.*
import com.google.gson.annotations.SerializedName


class SchedulePojo1: LightSchedulePojo1(), SchedulePojo {

    @SerializedName("start")
    override var start: Long = 0L

    @SerializedName("end")
    override var end: Long = 0L

    @SerializedName("cells")
    var cellPojos: List<CellPojo1> = listOf()


    override val cells: List<CellPojo>
        get() = cellPojos
}