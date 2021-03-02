package by.zenkevich_churun.findcell.domain.contract.sched

import by.zenkevich_churun.findcell.domain.entity.Cell
import by.zenkevich_churun.findcell.domain.entity.Jail
import com.google.gson.annotations.SerializedName


class CellPojo(

    @SerializedName("jailId")
    override var jailId: Int,

    @SerializedName("jailName")
    override var jailName: String,

    @SerializedName("number")
    override var number: Short,

    @SerializedName("seats")
    override var seats: Short

): Cell() {

    constructor(): this(Jail.UNKNOWN_ID, "", 0, 0)
}