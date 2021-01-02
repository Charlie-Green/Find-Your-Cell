package by.zenkevich_churun.findcell.server.internal.dao.auth

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class CheckUsernameQuery: LazyTypedQuery<Long>(
    "select count(*) from ${PrisonerEntity::class.java.simpleName} p where username=?0",
    Long::class.javaObjectType ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        username: String
    ): TypedQuery<Long> {

        return getTypedQuery(entityMan).apply {
            setParameter(0, username)
        }
    }
}