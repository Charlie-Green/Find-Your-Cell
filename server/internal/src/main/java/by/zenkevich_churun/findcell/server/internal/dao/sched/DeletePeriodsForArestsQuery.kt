package by.zenkevich_churun.findcell.server.internal.dao.sched

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import javax.persistence.EntityManager
import javax.persistence.Query


internal class DeletePeriodsForArestsQuery: LazyQuery(
    "delete from ${PeriodEntity::class.java.simpleName} where arestId in ?0" ) {

    fun getQuery(
        entityMan: EntityManager,
        arestIds: List<Int>
    ): Query = getQuery(entityMan).apply {
        setParameter(0, arestIds)
    }
}