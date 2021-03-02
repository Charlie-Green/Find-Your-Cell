package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.domain.entity.CoPrisoner
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity


/** A code of 3 binary digits.
  * The 1st digit is 0 if and only if a request has been sent and was declined.
  * The 2nd digit determines whether the first user has agreed for the connection,
  * and same is the 3rd digit for the second user.
  * By using such semantics and simple bitwise operations,
  * [CoPrisoner.Relation] mapping gets less error-prone. **/
internal class RelationCode private constructor(
    private var value: Int ) {

    /** Update [RelationCode] in response to the following:
      * The first user wishes to connect. **/
    fun set1() {
        value = value or 0b010   // Set 2nd digit to 1.
    }

    /** Update [RelationCode] in response to the following:
     * The second user wishes to connect. **/
    fun set2() {
        value = value or 0b001   // Set 3rd digit to 1.
    }

    /** Update [RelationCode] in response to the following:
     * The first user wishes to break the connection. **/
    fun unset1() {
        value = value and 0b001  // Set 1st and 2nd digits to 0.
    }

    /** Update [RelationCode] in response to the following:
      * The second user wishes to break the connection. **/
    fun unset2() {
        value = value and 0b010  // Set 1st and 3rd digits to 0.
    }


    /** Map this [RelationCode] back to [CoPrisoner.Relation] **/
    fun decode(): Short {
        return when(value) {
            0b000 -> CoPrisoner.Relation.SUGGESTED.ordinal.toShort()
            0b001 -> CoPrisoner.Relation.REQUEST_DECLINED.ordinal.toShort()
            0b010 -> CoPrisonerEntity.RELATION_ORDINAL_OUTCOMING_DECLINED
            0b011 -> CoPrisoner.Relation.CONNECTED.ordinal.toShort()
            0b100 -> CoPrisoner.Relation.SUGGESTED.ordinal.toShort()
            0b101 -> CoPrisoner.Relation.INCOMING_REQUEST.ordinal.toShort()
            0b110 -> CoPrisoner.Relation.OUTCOMING_REQUEST.ordinal.toShort()
            0b111 -> CoPrisoner.Relation.CONNECTED.ordinal.toShort()
            else -> throw Error("Internal error: unexpected RelationCode $value")
        }
    }


    companion object {

        fun encode(relationOrdinal: Short): RelationCode {
            val value = when(relationOrdinal.toInt()) {
                CoPrisoner.Relation.SUGGESTED.ordinal                        -> 0b100
                CoPrisoner.Relation.INCOMING_REQUEST.ordinal                 -> 0b101
                CoPrisoner.Relation.REQUEST_DECLINED.ordinal                 -> 0b001
                CoPrisoner.Relation.OUTCOMING_REQUEST.ordinal                -> 0b110
                CoPrisoner.Relation.CONNECTED.ordinal                        -> 0b111
                CoPrisonerEntity.RELATION_ORDINAL_OUTCOMING_DECLINED.toInt() -> 0b010
                else -> throw IllegalArgumentException("Invalid ordinal $relationOrdinal")
            }

            return RelationCode(value)
        }
    }
}