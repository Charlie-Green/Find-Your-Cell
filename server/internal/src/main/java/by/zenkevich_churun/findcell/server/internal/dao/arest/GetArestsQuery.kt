package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class GetArestsQuery: LazyTypedQuery<ArestEntity>(
    "select a from ${ArestEntity::class.java.simpleName} a where prisoner=?0",
    ArestEntity::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        prisonerId: Int
    ): TypedQuery<ArestEntity> = getTypedQuery(entityMan).apply {
        setParameter(0, prisonerId)
    }
}