package by.sviazen.server.internal.entity.view

import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner
import by.sviazen.server.internal.entity.table.ContactEntity
import javax.persistence.*


/** Combines data from Prisoners and Contacts table to implement [Prisoner] the abstract class. **/
@Entity
@Table(name = "Prisoners")
class PrisonerView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = Prisoner.INVALID_ID

    @Column(name = "username")
    var username: String? = null

    @Column(name = "pass")
    var passwordHash: ByteArray? = null

    @Column(name = "name")
    var name: String = ""

    @Column(name = "info")
    var info: String = ""

    @OneToMany(targetEntity = ContactEntity::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "prisoner", referencedColumnName = "id")
    var contactEntities: Set<ContactEntity> = hashSetOf()
}