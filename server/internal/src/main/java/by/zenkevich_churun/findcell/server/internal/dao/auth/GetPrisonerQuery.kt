package by.zenkevich_churun.findcell.server.internal.dao.auth

import by.zenkevich_churun.findcell.server.internal.dao.common.LazyQuery
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


internal class GetPrisonerQuery: LazyQuery<PrisonerEntity>(
    "select p from ${PrisonerEntity::class.java.simpleName} p where username=?0 and pass=?1",
    PrisonerEntity::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        username: String,
        passwordHash: ByteArray
    ): TypedQuery<PrisonerEntity> {

        return getTypedQuery(entityMan).apply {
            setParameter(0, username)
            setParameter(1, passwordHash)
        }
    }
}