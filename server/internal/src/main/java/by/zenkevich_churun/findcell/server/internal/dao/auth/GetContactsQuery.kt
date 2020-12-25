package by.zenkevich_churun.findcell.server.internal.dao.auth

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.ContactEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class GetContactsQuery: LazyTypedQuery<ContactEntity>(
    "select c from ${ContactEntity::class.java.simpleName} c where prisoner=?0",
    ContactEntity::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        prisonerId: Int
    ): TypedQuery<ContactEntity> {

        return getTypedQuery(entityMan).apply {
            setParameter(0, prisonerId)
        }
    }
}