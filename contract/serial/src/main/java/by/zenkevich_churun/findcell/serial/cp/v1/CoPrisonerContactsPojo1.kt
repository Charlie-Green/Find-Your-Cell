package by.zenkevich_churun.findcell.serial.cp.v1

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.serial.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.serial.util.prisoner.PrisonerSerialUtil
import com.google.gson.annotations.SerializedName


class CoPrisonerContactsPojo1 {

    @SerializedName("info")
    var info: String = ""


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


    fun collectContacts(): List<Contact> {
        return mutableListOf<Contact>().apply {
            PrisonerSerialUtil.addIfDefined(this, contactPhone,     Contact.Type.PHONE)
            PrisonerSerialUtil.addIfDefined(this, contactTelegram,  Contact.Type.TELEGRAM)
            PrisonerSerialUtil.addIfDefined(this, contactViber,     Contact.Type.VIBER)
            PrisonerSerialUtil.addIfDefined(this, contactWhatsApp,  Contact.Type.WHATSAPP)
            PrisonerSerialUtil.addIfDefined(this, contactSkype,     Contact.Type.SKYPE)
            PrisonerSerialUtil.addIfDefined(this, contactVk,        Contact.Type.VK)
            PrisonerSerialUtil.addIfDefined(this, contactFacebook,  Contact.Type.FACEBOOK)
            PrisonerSerialUtil.addIfDefined(this, contactInstagram, Contact.Type.INSTAGRAM)
        }
    }
}