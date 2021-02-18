package by.zenkevich_churun.findcell.serial.prisoner.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Contact


internal fun MutableList<Contact>.addIfDefined(
    contactData: String?,
    contactType: Contact.Type ) {

    contactData ?: return
    val contact = ContactPojo1(contactType, contactData)
    add(contact)
}