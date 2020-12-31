package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import javax.persistence.EntityManager


internal class GetIntersectingArestsQuery: LazyTypedQuery<java.lang.Integer>(

    // Don't use between because it's inclusive (need exclusive compare)
    "select id from ${ArestEntity::class.java.simpleName} a " +
    "where (a.prisonerId = ?0) and (" +         // A intersects B  <=>  either:
        "(a.start > ?1 and a.start < ?2) " +    // - A.start between B.start and B.end
        "or (?1 > a.start and ?1 < a.end) " +   // - B.start between A.start and A.end
        "or (a.end > ?1 and a.end < ?2) " +     // - A.end   between B.start and B.end
        "or (?2 > a.start and ?2 < a.end) " +   // - B.end   between A.start and A.end
        "or (a.start = ?1 and a.end = ?2)" +    // - A equals B
    ")",

    java.lang.Integer::class.java ) {


    fun getTypedQuery(
        entityMan: EntityManager,
        prisonerId: Int,
        start: Long,
        end: Long
    ) = getTypedQuery(entityMan).apply {

        // Have to write toString() - otherwise get "Parameter doesn't match expected type" error.
        setParameter(0, prisonerId/*.toString()*/)
        setParameter(1, start/*.toString()*/)
        setParameter(2, end/*.toString()*/)
    }
}