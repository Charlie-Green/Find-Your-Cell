package by.zenkevich_churun.findcell.contract.prisoner.decode.v1

import by.zenkevich_churun.findcell.contract.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.contract.prisoner.entity.Contact
import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner
import by.zenkevich_churun.findcell.contract.prisoner.internal.InternalPrisonerContract1
import by.zenkevich_churun.findcell.contract.prisoner.util.protocol.ProtocolUtil
import com.google.gson.annotations.SerializedName


internal class PrisonerPojo1 {
    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_ID)
    var id: Int = 0

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_USERNAME)
    var username: String? = null

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_PASSWORD_HASH)
    lateinit var passwordBase64: String

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_NAME)
    lateinit var name: String

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_INFO)
    lateinit var info: String


    @SerializedName(PrisonerContract1.KEY_CONTACT_PHONE)
    var contactPhone: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_TELEGRAM)
    var contactTelegram: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_VIBER)
    var contactViber: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_WHATSAPP)
    var contactWhatsApp: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_VK)
    var contactVk: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_FACEBOOK)
    var contactFacebook: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_INSTAGRAM)
    var contactInstagram: String? = null



    fun toPrisoner() = Prisoner(
        id,
        username,
        ProtocolUtil.decodeBase64(passwordBase64),
        name,
        info,
        contactsList
    )


    private val contactsList: List<Contact>
        get() = mutableListOf<Contact>().apply {
            addIfDefined(contactPhone,     PrisonerContract1.CONTACT_TYPE_PHONE)
            addIfDefined(contactTelegram,  PrisonerContract1.CONTACT_TYPE_TELEGRAM)
            addIfDefined(contactViber,     PrisonerContract1.CONTACT_TYPE_VIBER)
            addIfDefined(contactWhatsApp,  PrisonerContract1.CONTACT_TYPE_WHATSAPP)
            addIfDefined(contactVk,        PrisonerContract1.CONTACT_TYPE_VK)
            addIfDefined(contactFacebook,  PrisonerContract1.CONTACT_TYPE_FACEBOOK)
            addIfDefined(contactInstagram, PrisonerContract1.CONTACT_TYPE_INSTAGRAM)
        }

    private fun MutableList<Contact>.addIfDefined(
        contactData: String?,
        contactType: Short ) {

        contactData ?: return
        val contact = Contact(contactType, contactData)
        add(contact)
    }
}