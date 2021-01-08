package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.server.internal.entity.table.ContactEntity
import javax.persistence.*


/** Combines data from Prisoners and Contacts table to implement [Prisoner] the abstract class. **/
@Entity
@Table(name = "Prisoners")
class PrisonerView: Prisoner() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    override var id: Int = Prisoner.INVALID_ID

    @Column(name = "username")
    override var username: String? = null

    @Column(name = "pass")
    override var passwordHash: ByteArray? = null

    @Column(name = "name")
    override var name: String = ""

    @Column(name = "info")
    override var info: String = ""

    @OneToMany(targetEntity = ContactEntity::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "prisoner", referencedColumnName = "id")
    var contactEntities: Set<ContactEntity> = hashSetOf()


    override val contacts: List<Contact>
        get() = contactEntities.toList()
}