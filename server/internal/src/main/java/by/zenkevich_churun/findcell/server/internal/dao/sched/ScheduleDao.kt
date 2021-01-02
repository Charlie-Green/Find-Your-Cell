package by.zenkevich_churun.findcell.server.internal.dao.sched

import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection


class ScheduleDao(private val connection: DatabaseConnection) {

    private val queryDeleteForArests = DeletePeriodsForArestsQuery()


    fun deleteForArests(arests: List<Int>) {
        connection.withTransaction { entityMan ->
            queryDeleteForArests
                .getQuery(entityMan, arests)
                .executeUpdate()
        }
    }
}