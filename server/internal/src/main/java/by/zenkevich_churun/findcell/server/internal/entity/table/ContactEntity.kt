package by.zenkevich_churun.findcell.server.internal.entity.table

import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.server.internal.entity.key.ContactKey
import javax.persistence.*


@Entity
@Table(name = "Contacts")
class ContactEntity(
    @EmbeddedId
    var key: ContactKey,

    @Column(name = "data")
    var data: String ) {

    constructor(): this(ContactKey(), "")


    companion object {

        fun fromContact(
            contact: Contact,
            prisonerId: Int
        ): ContactEntity {
            val key = ContactKey(prisonerId, contact.type.ordinal.toShort())
            return ContactEntity(key, contact.data)
        }
    }
}