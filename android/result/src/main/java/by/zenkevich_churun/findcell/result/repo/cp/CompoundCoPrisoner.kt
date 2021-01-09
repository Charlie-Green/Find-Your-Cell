package by.zenkevich_churun.findcell.result.repo.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerContactEntity
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity


internal class CompoundCoPrisoner(
    val cp: CoPrisonerEntity,
    override val contacts: List<CoPrisonerContactEntity>
): CoPrisoner() {

    override val id: Int
        get() = cp.id

    override val name: String
        get() = cp.name

    override val info: String
        get() = cp.info

    override val relation: Relation
        get() = cp.relation
}