package by.zenkevich_churun.findcell.serial.util.prisoner

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.serial.prisoner.v1.pojo.ContactPojo1


internal object PrisonerSerialUtil {

    fun addIfDefined(
        target: MutableList<Contact>,
        contactData: String?,
        contactType: Contact.Type ) {

        contactData ?: return
        val contact = ContactPojo1(contactType, contactData)
        target.add(contact)
    }
}