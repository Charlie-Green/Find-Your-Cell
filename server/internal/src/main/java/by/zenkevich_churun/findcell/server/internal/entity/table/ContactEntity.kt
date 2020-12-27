package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.entity.Contact
import by.zenkevich_churun.findcell.server.internal.entity.key.ContactKey
import javax.persistence.*


@Entity
@Table(name = "Contacts")
class ContactEntity: Contact() {

    @EmbeddedId
    lateinit var key: ContactKey

    @Column(name = "data")
    override lateinit var data: String

    override val type: Type
        get() {
            val ordinal = key.type.toInt()
            return Contact.Type.values()[ordinal]
        }


    companion object {

        fun fromContact(
            contact: Contact,
            prisonerId: Int
        ): ContactEntity {

            if(contact is ContactEntity) {
                contact.key.prisonerId = prisonerId
                return contact
            }

            return ContactEntity().apply {
                key = ContactKey()
                key.prisonerId = prisonerId
                key.type = contact.type.ordinal.toShort()

                data = contact.data
            }
        }
    }
}