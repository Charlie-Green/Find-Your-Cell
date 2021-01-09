package by.zenkevich_churun.findcell.result.ui.cps

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.R


internal object CoPrisonerRelationIcons {

    private val icons by lazy {
        createIcons()
    }


    fun iconResourceFor(relation: CoPrisoner.Relation): Int {
        return icons[relation.ordinal]
    }


    private fun createIcons(): IntArray {
        return IntArray(CoPrisoner.Relation.values().size) { ordinal ->
            when(ordinal) {
                CoPrisoner.Relation.SUGGESTED.ordinal         -> R.drawable.relation_suggested
                CoPrisoner.Relation.INCOMING_REQUEST.ordinal  -> R.drawable.relation_in_request
                CoPrisoner.Relation.OUTCOMING_REQUEST.ordinal -> R.drawable.relation_out_request
                CoPrisoner.Relation.CONNECTED.ordinal         -> R.drawable.relation_connected
                else -> 0
            }
        }
    }
}