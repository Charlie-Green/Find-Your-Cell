package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import javax.persistence.EntityManager


internal class GetIntersectingArestsQuery: LazyTypedQuery<java.lang.Integer>(
    "select id from ${ArestEntity::class.java.simpleName} a where ('prisoner' = ?2) and not ('end' <= ?0 or 'start' >= ?1)",
    java.lang.Integer::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        prisonerId: Int,
        start: Long,
        end: Long
    ) = getTypedQuery(entityMan).apply {

        // Note 1. A !intersects B  <=>  (A.end <= B.start) || (A.start >= B.end)
        // Note 2. Have to write toString() - otherwise get "Parameter doesn't match expected type" error.
        setParameter(0, start.toString())
        setParameter(1, end.toString())
        setParameter(2, prisonerId.toString())
    }
}