package by.zenkevich_churun.findcell.domain.contract.prisoner

import by.zenkevich_churun.findcell.domain.contract.auth.AuthorizedPrisonerPojo
import by.zenkevich_churun.findcell.domain.entity.Prisoner
import com.google.gson.annotations.SerializedName


/** client -> server to update a [Prisoner] **/
class UpdatedPrisonerPojo(

    id: Int,

    @SerializedName("pass")
    var passwordBase64: String,

    name: String,

    info: String,

    phone: String?,

    telegram: String?,

    viber: String?,

    whatsapp: String?,

    vk: String?,

    skype: String?,

    facebook: String?,

    instagram: String?

): AuthorizedPrisonerPojo(id, name, info, phone, telegram, viber, whatsapp, vk, skype, facebook, instagram) {

    constructor(): this(
        Prisoner.INVALID_ID, "", "", "", null, null, null, null, null, null, null, null )


    companion object {

        fun from(
            prisoner: Prisoner,
            passwordBase64: String
        ): UpdatedPrisonerPojo {

            val pojo = UpdatedPrisonerPojo()
            pojo.id = prisoner.id
            pojo.name = prisoner.name
            pojo.passwordBase64 = passwordBase64
            for(contact in prisoner.contacts) {
                pojo.addContact(contact.type, contact.data)
            }

            return pojo
        }
    }
}