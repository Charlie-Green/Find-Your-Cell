package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** A code of 3 binary digits.
  * The 1st digit is only used to distinguish between
  * [CoPrisoner.Relation.INCOMING_REQUEST] (0) and [CoPrisoner.Relation.REQUEST_DECLINED] (1).
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
        value = value and 0b101  // Set 2nd digit to 0.
    }

    /** Update [RelationCode] in response to the following:
      * The second user wishes to break the connection. **/
    fun unset2() {
        value = value and 0b110  // Set 3rd digit to 0.
    }


    /** Map this [RelationCode] back to [CoPrisoner.Relation] **/
    fun decode(): CoPrisoner.Relation {
        val maskedValue = value and 0b11 // Check only the 2 last digits
        return when(maskedValue) {
            0b00 -> CoPrisoner.Relation.SUGGESTED
            0b10 -> CoPrisoner.Relation.OUTCOMING_REQUEST
            0b11 -> CoPrisoner.Relation.CONNECTED

            else -> {
                if(value == maskedValue /* 1st digit is 0 */) {
                    CoPrisoner.Relation.INCOMING_REQUEST
                } else {
                    CoPrisoner.Relation.REQUEST_DECLINED
                }
            }
        }
    }


    companion object {
        fun encode(relation: CoPrisoner.Relation): RelationCode {
            val value = when(relation) {
                CoPrisoner.Relation.SUGGESTED         -> 0b000
                CoPrisoner.Relation.INCOMING_REQUEST  -> 0b001
                CoPrisoner.Relation.REQUEST_DECLINED  -> 0b101
                CoPrisoner.Relation.OUTCOMING_REQUEST -> 0b010
                CoPrisoner.Relation.CONNECTED         -> 0b011
            }

            return RelationCode(value)
        }
    }
}