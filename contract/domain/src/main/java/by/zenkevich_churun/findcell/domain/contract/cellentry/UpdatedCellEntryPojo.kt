package by.zenkevich_churun.findcell.domain.contract.cellentry

import by.zenkevich_churun.findcell.domain.entity.*
import com.google.gson.annotations.SerializedName


/** client -> server to replace a [Cell] within a [Schedule]. **/
class UpdatedCellEntryPojo(

    @SerializedName("pass")
    var passwordBase64: String,

    @SerializedName("arest")
    var arestId: Int,

    @SerializedName("jail1")
    var oldJailId: Int,

    @SerializedName("cell1")
    var oldCellNumber: Short,

    @SerializedName("jail2")
    var newJailId: Int,

    @SerializedName("cell2")
    var newCellNumber: Short ) {


    constructor(): this(
        "", Arest.INVALID_ID, Jail.UNKNOWN_ID, -1, Jail.UNKNOWN_ID, -1 )
}