package by.zenkevich_churun.findcell.serial.sched.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.serial.sched.pojo.TwoCellEntriesPojo
import com.google.gson.annotations.SerializedName


class TwoCellEntriesPojo1: TwoCellEntriesPojo() {

    @SerializedName("arest")
    override var arestId: Int = Arest.INVALID_ID

    @SerializedName("pass")
    override var passwordBase64: String? = null

    @SerializedName("jail1")
    override var oldJailId: Int = Jail.UNKNOWN_ID

    @SerializedName("cell1")
    override var oldCellNumber: Short = -1

    @SerializedName("jail2")
    override var newJailId: Int = Jail.UNKNOWN_ID

    @SerializedName("cell2")
    override var newCellNumber: Short = 0
}