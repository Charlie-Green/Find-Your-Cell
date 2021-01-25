package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** A code of 2 binary digits.
  * The first digit determines whether the first user has agreed for the connection,
  * and same is the second digit for the second user.
  * By using such semantics and simple bitwise operations,
  * [CoPrisoner.Relation] mapping gets less error-prone. **/
internal class RelationCode private constructor(
    private var value: Int ) {

    /** Update [RelationCode] in response to the following:
      * The first user wishes to connect. **/
    fun set1() {
        value = value or 0b10
    }

    /** Update [RelationCode] in response to the following:
     * The second user wishes to connect. **/
    fun set2() {
        value = value or 0b01
    }

    /** Update [RelationCode] in response to the following:
     * The first user wishes to break the connection. **/
    fun unset1() {
        value = value and 0b01
    }

    /** Update [RelationCode] in response to the following:
      * The second user wishes to break the connection. **/
    fun unset2() {
        value = value and 0b10
    }


    /** Map this [RelationCode] back to [CoPrisoner.Relation] **/
    fun decode(): CoPrisoner.Relation {
        return when(value) {
            0b00 -> CoPrisoner.Relation.SUGGESTED
            0b01 -> CoPrisoner.Relation.INCOMING_REQUEST
            0b10 -> CoPrisoner.Relation.OUTCOMING_REQUEST
            else -> CoPrisoner.Relation.CONNECTED
        }
    }


    companion object {
        fun encode(relation: CoPrisoner.Relation): RelationCode {
            val value = when(relation) {
                CoPrisoner.Relation.SUGGESTED         -> 0b00
                CoPrisoner.Relation.INCOMING_REQUEST  -> 0b01
                CoPrisoner.Relation.OUTCOMING_REQUEST -> 0b10
                CoPrisoner.Relation.CONNECTED         -> 0b11
            }

            return RelationCode(value)
        }
    }
}