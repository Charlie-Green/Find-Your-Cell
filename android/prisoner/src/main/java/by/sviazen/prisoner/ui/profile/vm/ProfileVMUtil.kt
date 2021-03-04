package by.sviazen.prisoner.ui.profile.vm

import by.sviazen.domain.entity.Contact
import by.sviazen.domain.simpleentity.SimpleContact


internal object ProfileVMUtil {

    fun createContact(
        type: Contact.Type,
        existingContacts: Collection<Contact>
    ): Contact {

        val autofilledData = when(type) {
            Contact.Type.TELEGRAM  -> "t.me/"
            Contact.Type.SKYPE     -> "live:"
            Contact.Type.VK        -> "+375"
            Contact.Type.VIBER     -> phoneNumber(existingContacts)
            Contact.Type.WHATSAPP  -> phoneNumber(existingContacts)
            else                   -> ""
        }

        return SimpleContact(type, autofilledData)
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
            if(contact.type == Contact.Type.PHONE) {
                return contact.data
            }
        }

        return ""
    }
}