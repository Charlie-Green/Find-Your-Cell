package by.zenkevich_churun.findcell.serial.cp.v1

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.serial.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.addIfDefined
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
            addIfDefined(contactPhone,     Contact.Type.PHONE)
            addIfDefined(contactTelegram,  Contact.Type.TELEGRAM)
            addIfDefined(contactViber,     Contact.Type.VIBER)
            addIfDefined(contactWhatsApp,  Contact.Type.WHATSAPP)
            addIfDefined(contactSkype,     Contact.Type.SKYPE)
            addIfDefined(contactVk,        Contact.Type.VK)
            addIfDefined(contactFacebook,  Contact.Type.FACEBOOK)
            addIfDefined(contactInstagram, Contact.Type.INSTAGRAM)
        }
    }
}