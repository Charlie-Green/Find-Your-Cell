package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import by.zenkevich_churun.findcell.core.entity.Contact


internal object ProfileFragmentUtil {

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
}