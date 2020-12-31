package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import javax.persistence.EntityManager


internal class GetIntersectingArestsQuery: LazyTypedQuery<java.lang.Integer>(

    // A intersects B  <=>  (A.start between B.start and B.end) or (B.start between A.start and A.end)
    // Don't use between because it's inclusive (need exclusive compare)
    "select id from ${ArestEntity::class.java.simpleName} a "+
    "where ('prisoner' = ?0) and ( ('start' > ?1 and 'start' < ?2) or (?1 > 'start' and ?1 < 'end') )",

    java.lang.Integer::class.java ) {


    fun getTypedQuery(
        entityMan: EntityManager,
        prisonerId: Int,
        start: Long,
        end: Long
    ) = getTypedQuery(entityMan).apply {

        // Have to write toString() - otherwise get "Parameter doesn't match expected type" error.
        setParameter(0, prisonerId.toString())
        setParameter(1, start.toString())
        setParameter(2, end.toString())
    }
}