package by.zenkevich_churun.findcell.domain.contract.cp

import by.zenkevich_churun.findcell.domain.entity.Prisoner
import com.google.gson.annotations.SerializedName


class CoPrisonerHeaderPojo(

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("jail")
    var commonJailName: String,

    @SerializedName("cell")
    var commonCellNumber: Short,

    @SerializedName("rel")
    var relationOrdinal: Short ) {


    constructor(): this(
        Prisoner.INVALID_ID, "", "", -1, -1)
}