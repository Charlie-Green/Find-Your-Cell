package by.zenkevich_churun.findcell.server.internal.dao.common

import by.zenkevich_churun.findcell.server.internal.dao.internal.LazyTypedQuery
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


class ValidateCredentialsQuery: LazyTypedQuery<PrisonerEntity>(
    "select p from ${PrisonerEntity::class.java.simpleName} p where id=?0 and pass=?1",
    PrisonerEntity::class.java ) {

    fun getTypedQuery(
        entityMan: EntityManager,
        id: Int,
        passwordHash: ByteArray
    ): TypedQuery<PrisonerEntity> {

        return getTypedQuery(entityMan).apply {
            setParameter(0, id)
            setParameter(1, passwordHash)
        }
    }
}