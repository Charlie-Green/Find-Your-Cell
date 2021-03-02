package by.zenkevich_churun.findcell.domain.contract.arest

import by.zenkevich_churun.findcell.domain.entity.Arest
import by.zenkevich_churun.findcell.domain.entity.Prisoner
import com.google.gson.annotations.SerializedName


/** client -> server to create an [Arest]. **/
class AddedArestPojo(

    @SerializedName("prisoner")
    var prisonerId: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    @SerializedName("start")
    var start: Long,

    @SerializedName("end")
    var end: Long ) {


    constructor(): this(Prisoner.INVALID_ID, "", 0L, 0L)
}