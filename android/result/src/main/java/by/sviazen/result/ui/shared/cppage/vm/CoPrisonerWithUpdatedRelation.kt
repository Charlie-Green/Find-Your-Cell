package by.sviazen.result.ui.shared.cppage.vm

import by.sviazen.domain.entity.CoPrisoner


/** Wrapper for a [CoPrisoner] instance returning a predefined [CoPrisoner.Relation]
  * and delegating the rest of the properties to the underlying implementation **/
internal class CoPrisonerWithUpdatedRelation private constructor(
    override val id: Int,
    override val name: String,
    override val commonJailName: String,
    override val commonCellNumber: Short,
    override val relation: Relation
): CoPrisoner() {


    companion object {

        fun wrap(
            cp: CoPrisoner,
            newRelation: CoPrisoner.Relation
        ) = CoPrisonerWithUpdatedRelation(
            cp.id,
            cp.name,
            cp.commonJailName,
            cp.commonCellNumber,
            newRelation
        )
    }
}