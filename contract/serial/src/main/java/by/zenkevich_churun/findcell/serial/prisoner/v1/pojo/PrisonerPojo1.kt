package by.zenkevich_churun.findcell.serial.prisoner.v1.pojo

import by.zenkevich_churun.findcell.serial.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
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

    @SerializedName(PrisonerContract1.KEY_CONTACT_SKYPE)
    var contactSkype: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_VK)
    var contactVk: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_FACEBOOK)
    var contactFacebook: String? = null

    @SerializedName(PrisonerContract1.KEY_CONTACT_INSTAGRAM)
    var contactInstagram: String? = null


    override var passwordHash: ByteArray?
        get() {
            val base64 = passwordBase64
                ?: throw NullPointerException("Password hash not specified")
            return ProtocolUtil.decodeBase64(base64)
        }
        set(value) {
            passwordBase64 = value?.let {
                ProtocolUtil.encodeBase64(it)
            }
        }


    override val contacts: List<Contact>
        get() = mutableListOf<Contact>().apply {
            addIfDefined(contactPhone,     Contact.Type.PHONE)
            addIfDefined(contactTelegram,  Contact.Type.TELEGRAM)
            addIfDefined(contactViber,     Contact.Type.VIBER)
            addIfDefined(contactWhatsApp,  Contact.Type.WHATSAPP)
            addIfDefined(contactSkype,     Contact.Type.SKYPE)
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


    companion object {

        fun from(p: Prisoner): PrisonerPojo1 {
            if(p is PrisonerPojo1) {
                return p
            }

            return PrisonerPojo1().apply {
                id = p.id
                username = p.username
                passwordHash = p.passwordHash
                name = p.name
                info = p.info

                for(c in p.contacts) {
                    when(c.type) {
                        Contact.Type.PHONE     -> { contactPhone     = c.data }
                        Contact.Type.TELEGRAM  -> { contactTelegram  = c.data }
                        Contact.Type.VIBER     -> { contactViber     = c.data }
                        Contact.Type.WHATSAPP  -> { contactWhatsApp  = c.data }
                        Contact.Type.SKYPE     -> { contactSkype     = c.data }
                        Contact.Type.VK        -> { contactVk        = c.data }
                        Contact.Type.FACEBOOK  -> { contactFacebook  = c.data }
                        Contact.Type.INSTAGRAM -> { contactInstagram = c.data }
                    }
                }
            }
        }
    }
}