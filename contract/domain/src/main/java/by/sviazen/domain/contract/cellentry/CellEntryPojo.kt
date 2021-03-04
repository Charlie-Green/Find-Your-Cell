package by.sviazen.domain.contract.cellentry

import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Jail
import by.sviazen.domain.entity.Schedule
import com.google.gson.annotations.SerializedName


/** client -> server to add/delete a [Cell] to, or delete one from, a [Schedule]) **/
class CellEntryPojo(

    @SerializedName("pass")
    var passwordBase64: String,

    @SerializedName("arest")
    var arestId: Int,

    @SerializedName("jail")
    var jailId: Int,

    @SerializedName("cell")
    var cellNumber: Short ) {


    constructor(): this("", Arest.INVALID_ID, Jail.UNKNOWN_ID, -1)
}