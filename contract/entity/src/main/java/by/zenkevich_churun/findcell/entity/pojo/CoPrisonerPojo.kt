package by.zenkevich_churun.findcell.entity.pojo

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


abstract class CoPrisonerPojo: CoPrisoner() {

    abstract var relationOrdinal: Int


    override val relation: Relation
        get() = Relation.values()[relationOrdinal]
}