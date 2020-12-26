package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity


class ArestsDao(private val connection: DatabaseConnection) {

    private val queryGetArests = GetArestsQuery()


    fun getArests(prisonerId: Int): List<ArestEntity> {
        val q = queryGetArests.getTypedQuery(connection.entityMan, prisonerId)
        return  q.resultList
    }
}