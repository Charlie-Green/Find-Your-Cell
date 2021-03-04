package by.sviazen.server.internal.repo.cp

import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.server.internal.entity.table.CoPrisonerEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class RelationCodeTest {

    @Test
    fun suggestedToOutcomingRequest() = test(
        CoPrisoner.Relation.SUGGESTED,
        CoPrisoner.Relation.OUTCOMING_REQUEST
    ) { rc -> rc.set1() }

    @Test
    fun suggestedToIncomingRequest() = test(
        CoPrisoner.Relation.SUGGESTED,
        CoPrisoner.Relation.INCOMING_REQUEST
    ) { rc -> rc.set2() }

    @Test
    fun iDeclineRequest() = test(
        CoPrisoner.Relation.INCOMING_REQUEST,
        CoPrisoner.Relation.REQUEST_DECLINED
    ) { rc -> rc.unset1() }

    @Test
    fun otherDeclinesRequest() = test(
        CoPrisoner.Relation.OUTCOMING_REQUEST.ordinal.toShort(),
        CoPrisonerEntity.RELATION_ORDINAL_OUTCOMING_DECLINED
    ) { rc -> rc.unset2() }

    @Test
    fun iBreakConnection() = test(
        CoPrisoner.Relation.CONNECTED,
        CoPrisoner.Relation.REQUEST_DECLINED
    ) { rc -> rc.unset1() }

    @Test
    fun otherBreaksConnection() = test(
        CoPrisoner.Relation.CONNECTED.ordinal.toShort(),
        CoPrisonerEntity.RELATION_ORDINAL_OUTCOMING_DECLINED
    ) { rc -> rc.unset2() }


    private fun test(
        initialRelation: Short,
        expectedRelation: Short,
        action: (RelationCode) -> Unit ) {

        val relationCode = RelationCode.encode(initialRelation)
        action(relationCode)
        val actualRelation = relationCode.decode()
        Assertions.assertEquals(expectedRelation, actualRelation)
    }

    private fun test(
        initialRelation: CoPrisoner.Relation,
        expectedRelation: CoPrisoner.Relation,
        action: (RelationCode) -> Unit

    ) = test(
        initialRelation.ordinal.toShort(),
        expectedRelation.ordinal.toShort(),
        action
    )
}