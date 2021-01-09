package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.entity.Contact


class CoPrisonerView(
    val prisonerView: PrisonerView,
    override val relation: Relation
): CoPrisoner() {

    override val id: Int
        get() = prisonerView.id

    override val name: String
        get() = prisonerView.name

    override val info: String
        get() = prisonerView.info

    override val contacts: List<Contact>
        get() = prisonerView.contacts
}