package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.*
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import javax.persistence.Column


class CoPrisonerView(
    override val id: Int,
    override val name: String,
    override val commonJailName: String,
    override val commonCellNumber: Short,
    override val relation: Relation
): CoPrisoner()


// TODO: CharlieDebug
//class CoPrisonerView(
//    val prisonerView: PrisonerView,
//    override val relation: Relation,
//    override val commonJailName: String,
//    override val commonCellNumber: Short
//): CoPrisoner() {
//
//    override val id: Int
//        get() = prisonerView.id
//
//    override val name: String
//        get() = prisonerView.name
//
//    override val info: String
//        get() = prisonerView.info
//}