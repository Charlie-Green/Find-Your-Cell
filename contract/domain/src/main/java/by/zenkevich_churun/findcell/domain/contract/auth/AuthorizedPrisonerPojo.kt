package by.zenkevich_churun.findcell.domain.contract.auth

import by.zenkevich_churun.findcell.domain.entity.Prisoner
import com.google.gson.annotations.SerializedName


/** server -> client to log in or sign up. **/
open class AuthorizedPrisonerPojo(

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("info")
    var info: String,

    @SerializedName("phone")
    var phone: String?,

    @SerializedName("tg")
    var telegram: String?,

    @SerializedName("viber")
    var viber: String?,

    @SerializedName("wapp")
    var whatsapp: String?,

    @SerializedName("vk")
    var vk: String?,

    @SerializedName("skype")
    var skype: String?,

    @SerializedName("fb")
    var facebook: String?,

    @SerializedName("insta")
    var instagram: String? ) {


    constructor(): this(
        Prisoner.INVALID_ID, "", "", null, null, null, null, null, null, null, null )
}