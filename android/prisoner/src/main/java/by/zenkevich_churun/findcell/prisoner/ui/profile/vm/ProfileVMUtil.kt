package by.zenkevich_churun.findcell.prisoner.ui.profile.vm

import by.zenkevich_churun.findcell.core.entity.Contact


internal object ProfileVMUtil {

    fun createContact(
        type: Contact.Type,
        existingContacts: Collection<Contact>
    ): Contact {

        return when(type) {
            Contact.Type.TELEGRAM  -> Contact.Telegram("t.me/")
            Contact.Type.SKYPE     -> Contact.Skype("live:")
            Contact.Type.VK        -> Contact.VK("")
            Contact.Type.FACEBOOK  -> Contact.Facebook("")
            Contact.Type.INSTAGRAM -> Contact.Instagram("")
            Contact.Type.PHONE     -> Contact.Phone("+375 ")
            Contact.Type.VIBER     -> Contact.Viber(phoneNumber(existingContacts))
            Contact.Type.WHATSAPP  -> Contact.WhatsApp(phoneNumber(existingContacts))
        }
    }

    /** Obtains list of [Contact.Type]s the user is suggested to add
     * based on the list of existing [Contact]s. **/
    fun addedContactTypes(existingContacts: Collection<Contact>): MutableList<Contact.Type> {
        val result = mutableListOf<Contact.Type>()

        for(type in Contact.Type.values()) {
            val existingContact = existingContacts.find { contact ->
                contact.type == type
            }

            if(existingContact == null) {
                result.add(type)
            }
        }

        return result
    }


    private fun phoneNumber(contacts: Collection<Contact>): String {
        for(contact in contacts) {
            if(contact is Contact.Phone) {
                return contact.data
            }
        }

        return ""
    }
}