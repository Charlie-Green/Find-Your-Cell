package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity


/** Given [CoPrisoner.Relation] ordinal as in database,
  * returns [CoPrisoner.Relation] to be sent to the client. **/
internal class RelationResolver(private val ordinal: Short) {

    fun resolve(relativeToFirst: Boolean): CoPrisoner.Relation {
        if(relativeToFirst) {
            return resolveRelativeToFirst()
        }
        return resolveRelativeToSecond()
    }


    private fun resolveRelativeToFirst(): CoPrisoner.Relation {
        if(ordinal == CoPrisonerEntity.RELATION_ORDINAL_OUTCOMING_DECLINED) {
            return CoPrisoner.Relation.OUTCOMING_REQUEST
        }
        return CoPrisoner.Relation.values()[ordinal.toInt()]
    }

    private fun resolveRelativeToSecond(): CoPrisoner.Relation {
        if(ordinal.toInt() == CoPrisoner.Relation.INCOMING_REQUEST.ordinal) {
            return CoPrisoner.Relation.OUTCOMING_REQUEST
        }
        if(ordinal.toInt() == CoPrisoner.Relation.OUTCOMING_REQUEST.ordinal) {
            return CoPrisoner.Relation.INCOMING_REQUEST
        }
        if(ordinal == CoPrisonerEntity.RELATION_ORDINAL_OUTCOMING_DECLINED) {
            return CoPrisoner.Relation.REQUEST_DECLINED
        }

        try {
            return CoPrisoner.Relation.values()[ordinal.toInt()]
        } catch(exc: IndexOutOfBoundsException) {
            throw IllegalArgumentException("Invalid relation ordinal $ordinal")
        }
    }
}