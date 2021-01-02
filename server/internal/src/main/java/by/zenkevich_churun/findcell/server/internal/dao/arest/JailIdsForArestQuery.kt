package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class JailIdsForArestQuery: LazyTypedQuery<Int>(
    "select distinct jailId from ${PeriodEntity::class.java.simpleName} p where arest=?0",
    Int::class.javaObjectType ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        arestId: Int
    ) = getTypedQuery(entityMan).apply {
        setParameter(0, arestId)
    }
}