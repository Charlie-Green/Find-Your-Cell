package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import javax.persistence.PersistenceException


class ArestsDao(private val connection: DatabaseConnection) {

    private val queryGetArests = GetArestsQuery()
    private val queryJailsForArest = JailIdsForArestQuery()


    fun arests(prisonerId: Int): List<ArestEntity> {
        val q = queryGetArests.getTypedQuery(connection.entityMan, prisonerId)
        return  q.resultList
    }

    fun arests(
        prisonerId: Int,
        start: Long,
        end: Long
    ): List<ArestEntity> {

        TODO()
    }

    fun jailIds(arestId: Int): List<Int> {
        val q = queryJailsForArest.getTypedQuery(connection.entityMan, arestId)

        // TODO: Can this mapping be avoided?
        return q.resultList.map { javaInteger ->
            javaInteger.toInt()
        }
    }

    fun add(arest: ArestEntity) {
        connection.withTransaction { entityMan ->
            try {
                entityMan.persist(arest)
            } catch(exc: PersistenceException) {
                val notPersistMsg = "Could not persist an ${ArestEntity::class.java}"
                val idMessage = "Prisoner ID ${arest.prisonerId} is probably invalid"
                throw IllegalArgumentException("$notPersistMsg. $idMessage")
            }
        }
    }
}