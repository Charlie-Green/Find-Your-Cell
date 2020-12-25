package by.zenkevich_churun.findcell.server.internal.dao.profile

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyQuery
import by.zenkevich_churun.findcell.server.internal.entity.ContactEntity
import javax.persistence.*


internal class DeleteContactsQuery: LazyQuery(
    "delete from ${ContactEntity::class.java.simpleName} c where prisoner=?0" ) {

    fun getQuery(
        entityMan: EntityManager,
        prisonerId: Int
    ): Query = getQuery(entityMan).apply {
        setParameter(0, prisonerId)
    }
}