package by.sviazen.domain.contract.arest

import by.sviazen.domain.entity.Prisoner
import com.google.gson.annotations.SerializedName


/** client -> server to delete [Arest]s. **/
class DeletedArestsPojo(

    @SerializedName("prisoner")
    var prisonerId: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    @SerializedName("arests")
    var arestIds: List<Int> ) {


    constructor(): this(Prisoner.INVALID_ID, "", listOf())
}