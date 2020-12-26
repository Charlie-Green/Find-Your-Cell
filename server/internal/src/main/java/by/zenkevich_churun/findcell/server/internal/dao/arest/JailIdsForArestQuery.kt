package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class JailIdsForArestQuery: LazyTypedQuery<java.lang.Integer>(
    "select distinct jailId from ${PeriodEntity::class.java.simpleName} p where arest=?0",
    java.lang.Integer::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        arestId: Int
    ): TypedQuery<java.lang.Integer> = getTypedQuery(entityMan).apply {
        setParameter(0, arestId)
    }
}