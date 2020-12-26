package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity


class ArestsDao(private val connection: DatabaseConnection) {

    private val queryGetArests = GetArestsQuery()
    private val queryJailsForArest = JailIdsForArestQuery()


    fun arests(prisonerId: Int): List<ArestEntity> {
        val q = queryGetArests.getTypedQuery(connection.entityMan, prisonerId)
        return  q.resultList
    }

    fun jailIds(arestId: Int): List<Int> {
        val q = queryJailsForArest.getTypedQuery(connection.entityMan, arestId)

        // TODO: Can this mapping be avoided?
        return q.resultList.map { javaInteger ->
            javaInteger.toInt()
        }
    }
}