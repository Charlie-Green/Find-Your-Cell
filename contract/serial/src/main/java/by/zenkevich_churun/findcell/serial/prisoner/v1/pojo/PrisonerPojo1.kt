package by.zenkevich_churun.findcell.serial.prisoner.v1.pojo

import by.zenkevich_churun.findcell.serial.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.serial.prisoner.contract.InternalPrisonerContract1
import by.zenkevich_churun.findcell.serial.util.prisoner.PrisonerSerialUtil
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


    override val passwordHash: ByteArray?
        get() = null


    override val contacts: List<Contact>
        get() = mutableListOf<Contact>().apply {
            PrisonerSerialUtil.addIfDefined(this, contactPhone,     Contact.Type.PHONE)
            PrisonerSerialUtil.addIfDefined(this, contactTelegram,  Contact.Type.TELEGRAM)
            PrisonerSerialUtil.addIfDefined(this, contactViber,     Contact.Type.VIBER)
            PrisonerSerialUtil.addIfDefined(this, contactWhatsApp,  Contact.Type.WHATSAPP)
            PrisonerSerialUtil.addIfDefined(this, contactSkype,     Contact.Type.SKYPE)
            PrisonerSerialUtil.addIfDefined(this, contactVk,        Contact.Type.VK)
            PrisonerSerialUtil.addIfDefined(this, contactFacebook,  Contact.Type.FACEBOOK)
            PrisonerSerialUtil.addIfDefined(this, contactInstagram, Contact.Type.INSTAGRAM)
        }


    companion object {

        fun from(
            prisoner: Prisoner,
            passwordBase64: String?
        ): PrisonerPojo1 {

            if(prisoner is PrisonerPojo1) {
                return prisoner
            }

            val pojo = PrisonerPojo1()
            pojo.id = prisoner.id
            pojo.username = prisoner.username
            pojo.passwordBase64 = passwordBase64
            pojo.name = prisoner.name
            pojo.info = prisoner.info

            for(c in prisoner.contacts) {
                when(c.type) {
                    Contact.Type.PHONE     -> { pojo.contactPhone     = c.data }
                    Contact.Type.TELEGRAM  -> { pojo.contactTelegram  = c.data }
                    Contact.Type.VIBER     -> { pojo.contactViber     = c.data }
                    Contact.Type.WHATSAPP  -> { pojo.contactWhatsApp  = c.data }
                    Contact.Type.SKYPE     -> { pojo.contactSkype     = c.data }
                    Contact.Type.VK        -> { pojo.contactVk        = c.data }
                    Contact.Type.FACEBOOK  -> { pojo.contactFacebook  = c.data }
                    Contact.Type.INSTAGRAM -> { pojo.contactInstagram = c.data }
                }
            }

            return pojo
        }
    }
}