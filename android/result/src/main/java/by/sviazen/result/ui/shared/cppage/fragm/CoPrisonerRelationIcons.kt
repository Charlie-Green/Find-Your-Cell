package by.sviazen.result.ui.shared.cppage.fragm

import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.R


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
                CoPrisoner.Relation.REQUEST_DECLINED.ordinal  -> R.drawable.relation_request_decline
                CoPrisoner.Relation.OUTCOMING_REQUEST.ordinal -> R.drawable.relation_out_request
                CoPrisoner.Relation.CONNECTED.ordinal         -> R.drawable.relation_connected
                else -> 0
            }
        }
    }
}