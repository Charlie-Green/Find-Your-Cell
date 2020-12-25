package by.zenkevich_churun.findcell.server.internal.entity

import by.zenkevich_churun.findcell.protocol.prisoner.entity.Contact
import javax.persistence.*


@Entity
@Table(name = "Contacts")
class ContactEntity {

    @EmbeddedId
    lateinit var key: ContactKey

    @Column(name = "data")
    lateinit var data: String


    /** Converts this to [Contact] entity from Protocol the project. **/
    fun toContact()
        = Contact(key.type, data)


    companion object {

        fun fromContact(
            contact: Contact,
            prisonerId: Int
        ): ContactEntity {

            return ContactEntity().apply {
                key = ContactKey()
                key.prisonerId = prisonerId
                key.type = contact.type

                data = contact.data
            }
        }
    }
}