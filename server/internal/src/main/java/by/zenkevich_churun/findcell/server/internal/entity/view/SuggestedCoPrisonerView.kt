package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.*
import by.zenkevich_churun.findcell.server.internal.entity.table.ContactEntity
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import javax.persistence.*


/** Unlike [CoPrisonerView], this is selected from the table of [PrisonerEntity]s.
  * [relation] property returns [CoPrisoner.Relation.SUGGESTED]. **/
@Entity
@Table(name = "Prisoners")
class SuggestedCoPrisonerView: CoPrisoner() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    override var id: Int = Prisoner.INVALID_ID

    @Column(name = "name")
    override var name: String = ""

    @OneToMany(targetEntity = ContactEntity::class)
    @JoinTable(name = "Contacts")
    @JoinColumn(name = "prisoner", referencedColumnName = "id")
    var contactsSet: Set<ContactEntity> = hashSetOf()

    override val relation: CoPrisoner.Relation = CoPrisoner.Relation.SUGGESTED

    override val contacts: List<Contact>
        get() = contactsSet.toList()
}