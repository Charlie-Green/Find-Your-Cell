package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.serial.sched.pojo.CellEntryPojo
import com.google.gson.annotations.SerializedName


class CellEntryPojo1: CellEntryPojo() {

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("arest")
    override var arestId: Int = Arest.INVALID_ID

    @SerializedName("jail")
    override var jailId: Int = Jail.UNKNOWN_ID

    @SerializedName("cell")
    override var cellNumber: Short = -1
}