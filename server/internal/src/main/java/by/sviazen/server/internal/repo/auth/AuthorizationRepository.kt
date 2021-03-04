package by.sviazen.server.internal.repo.auth

import by.sviazen.domain.contract.auth.*
import by.sviazen.server.internal.entity.table.PrisonerEntity
import by.sviazen.server.internal.entity.view.PrisonerView
import by.sviazen.server.internal.repo.common.SviazenRepositiory
import javax.persistence.PersistenceException


class AuthorizationRepository: SviazenRepositiory() {

    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisonerView = prisonerDao.get(username, passwordHash)
        if(prisonerView != null) {
            return logInSuccess(prisonerView)
        }

        val usernameCount = prisonerDao.countByUsername(username)
        if(usernameCount == 0) {
            return LogInResponse.WrongUsername
        }

        return LogInResponse.WrongPassword
    }


    fun signUp(
        username: String,
        passwordHash: ByteArray,
        initialName: String
    ): SignUpResponse {

        val createdEntity = PrisonerEntity()
        createdEntity.username     = username
        createdEntity.passwordHash = passwordHash
        createdEntity.name         = initialName
        createdEntity.info         = ""

        try {
            prisonerDao.save(createdEntity)
        } catch(exc: PersistenceException) {
            // In the database there is a unique constraint on the username field.
            return SignUpResponse.UsernameTaken
        }

        val pojo = AuthorizedPrisonerPojo()
        pojo.id = createdEntity.id
        pojo.name = initialName

        return SignUpResponse.Success(pojo)
    }


    private fun logInSuccess(
        p: PrisonerView
    ): LogInResponse.Success {

        val pojo = AuthorizedPrisonerPojo()

        pojo.id = p.id
        pojo.name = p.name
        pojo.info = p.info

        for(contact in p.contactEntities) {
            pojo.addContact(
                ordinalToContactType(contact.key.typeOrdinal),
                contact.data
            )
        }

        return LogInResponse.Success(pojo)
    }
}