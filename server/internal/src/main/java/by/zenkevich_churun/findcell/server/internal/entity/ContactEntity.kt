package by.zenkevich_churun.findcell.server.internal.entity

import javax.persistence.*


@Entity
@Table(name = "Contacts")
class ContactEntity {

    @EmbeddedId
    lateinit var key: ContactKey

    @Column(name = "data")
    lateinit var data: String
}