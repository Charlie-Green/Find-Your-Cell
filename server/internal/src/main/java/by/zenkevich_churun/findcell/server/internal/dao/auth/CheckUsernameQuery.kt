package by.zenkevich_churun.findcell.server.internal.dao.auth

import by.zenkevich_churun.findcell.server.internal.dao.common.LazyQuery
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class CheckUsernameQuery: LazyQuery<java.lang.Long>(
    "select count(*) from ${PrisonerEntity::class.java.simpleName} p where username=?0",
    java.lang.Long::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        username: String
    ): TypedQuery<java.lang.Long> {

        return getTypedQuery(entityMan).apply {
            setParameter(0, username)
        }
    }
}