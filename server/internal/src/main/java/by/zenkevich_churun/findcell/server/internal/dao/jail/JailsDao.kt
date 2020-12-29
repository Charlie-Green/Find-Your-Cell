package by.zenkevich_churun.findcell.server.internal.dao.jail

import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection


class JailsDao(private val connection: DatabaseConnection) {

    private val queryGetJails = GetJailsQuery()


    fun getJails(): List<Jail> {
        return queryGetJails
            .getTypedQuery(connection.entityMan)
            .resultList
    }
}