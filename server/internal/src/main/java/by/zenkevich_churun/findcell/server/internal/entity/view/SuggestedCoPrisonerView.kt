package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.*
import by.zenkevich_churun.findcell.server.internal.entity.table.ContactEntity
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import javax.persistence.*


@Entity
@Table(name = "Prisoners")
class SuggestedCoPrisonerView: CoPrisoner() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    override var id: Int = Prisoner.INVALID_ID

    @Column(name = "name")
    override var name: String = ""

    @Column(name = "info")
    override var info: String = ""

    override val relation: CoPrisoner.Relation
        get() = CoPrisoner.Relation.SUGGESTED

    override val contacts: List<Contact>
        get() = listOf()  // 'Cause for Suggested Prisoner Contacts are hidden.


    fun toCoPrisonerView(): CoPrisonerView {
        val prisoner = PrisonerView()
        prisoner.id = id
        prisoner.name = name
        prisoner.info = info
        prisoner.contactEntities = setOf()

        return CoPrisonerView(prisoner, relation)
    }
}