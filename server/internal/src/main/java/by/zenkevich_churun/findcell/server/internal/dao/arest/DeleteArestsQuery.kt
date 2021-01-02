package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import javax.persistence.EntityManager
import javax.persistence.Query


internal class DeleteArestsQuery: LazyQuery(
    "delete from ${ArestEntity::class.java.simpleName} where id in ?0" ) {

    fun getQuery(
        entityMan: EntityManager,
        ids: List<Int>
    ): Query = getQuery(entityMan).apply {
        setParameter(0, ids)
    }
}