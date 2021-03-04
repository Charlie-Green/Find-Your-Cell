package by.sviazen.domain.contract.auth

import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner
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


    fun addContact(type: Contact.Type, data: String) {
        when(type) {
            Contact.Type.PHONE     -> phone     = data
            Contact.Type.TELEGRAM  -> telegram  = data
            Contact.Type.VIBER     -> viber     = data
            Contact.Type.WHATSAPP  -> whatsapp  = data
            Contact.Type.VK        -> vk        = data
            Contact.Type.SKYPE     -> skype     = data
            Contact.Type.FACEBOOK  -> facebook  = data
            Contact.Type.INSTAGRAM -> instagram = data
        }
    }
}