package by.zenkevich_churun.findcell.serial.prisoner.v1.pojo

import by.zenkevich_churun.findcell.serial.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.entity.Contact
import by.zenkevich_churun.findcell.entity.Prisoner
import by.zenkevich_churun.findcell.serial.prisoner.contract.InternalPrisonerContract1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import com.google.gson.annotations.SerializedName


internal class PrisonerPojo1: Prisoner() {
    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_ID)
    override var id: Int = 0

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_USERNAME)
    override var username: String? = null

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_PASSWORD_HASH)
    var passwordBase64: String? = null

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_NAME)
    override var name: String = ""

    @SerializedName(InternalPrisonerContract1.KEY_PRISONER_INFO)
    override var info: String = ""


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


    override val passwordHash: ByteArray?
        get() {
            val base64 = passwordBase64
                ?: throw NullPointerException("Password hash not specified")
            return ProtocolUtil.decodeBase64(base64)
        }


    override val contacts: List<Contact>
        get() = mutableListOf<Contact>().apply {
            addIfDefined(contactPhone,     Contact.Type.PHONE)
            addIfDefined(contactTelegram,  Contact.Type.TELEGRAM)
            addIfDefined(contactViber,     Contact.Type.VIBER)
            addIfDefined(contactWhatsApp,  Contact.Type.WHATSAPP)
            addIfDefined(contactVk,        Contact.Type.VK)
            addIfDefined(contactFacebook,  Contact.Type.FACEBOOK)
            addIfDefined(contactInstagram, Contact.Type.INSTAGRAM)
        }

    private fun MutableList<Contact>.addIfDefined(
        contactData: String?,
        contactType: Contact.Type ) {

        contactData ?: return
        val contact = ContactPojo1(contactType, contactData)
        add(contact)
    }
}