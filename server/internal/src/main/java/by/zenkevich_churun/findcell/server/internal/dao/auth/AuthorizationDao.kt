package by.zenkevich_churun.findcell.server.internal.dao.auth

import by.zenkevich_churun.findcell.server.internal.dao.common.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.entity.ContactEntity
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.util.ServerInternalUtil.optionalResult
import javax.persistence.EntityManager


class AuthorizationDao(
    private val connection: DatabaseConnection ) {

    private val queryGetPrisoner   = GetPrisonerQuery()
    private val queryCheckUsername = CheckUsernameQuery()
    private val queryGetContacts   = GetContactsQuery()


    fun getPrisoner(
        username: String,
        passwordHash: ByteArray
    ): PrisonerEntity? {

        val q = queryGetPrisoner.getTypedQuery(
            entityMan,
            username,
            passwordHash
        )
        return q.optionalResult
    }

    fun checkUsername(username: String): Boolean {
        val q = queryCheckUsername.getTypedQuery(entityMan, username)
        return q.singleResult >= 1
    }

    fun getContacts(prisonerId: Int): List<ContactEntity> {
        val q = queryGetContacts.getTypedQuery(entityMan, prisonerId)
        return q.resultList
    }

    fun addPrisoner(
        username: String,
        passwordHash: ByteArray,
        initialName: String
    ): Int {

        val entity          = PrisonerEntity()
        entity.username     = username
        entity.passwordHash = passwordHash
        entity.name         = initialName
        entity.info         = ""

        connection.withTransaction { man ->
            man.persist(entity)
        }

        return entity.id
    }


    private val entityMan: EntityManager
        get() = connection.entityMan
}