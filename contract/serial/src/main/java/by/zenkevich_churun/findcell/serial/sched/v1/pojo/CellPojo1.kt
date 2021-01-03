package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.serial.sched.pojo.CellPojo
import com.google.gson.annotations.SerializedName


class CellPojo1: CellPojo() {

    @SerializedName("jail")
    override var jailId: Int = 0

    @SerializedName("cell")
    override var cellNumber: Short = 0
}