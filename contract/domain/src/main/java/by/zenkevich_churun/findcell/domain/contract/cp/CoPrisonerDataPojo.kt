package by.zenkevich_churun.findcell.domain.contract.cp

import com.google.gson.annotations.SerializedName


/** server -> client to get [CoPrisoner]'s private data.
  * Must be restricted for non-connnected [CoPrisoner]s. **/
class CoPrisonerDataPojo(

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
        "", null, null, null, null, null, null, null, null )
}