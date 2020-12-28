package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.entity.response.LogInResponse
import by.zenkevich_churun.findcell.entity.response.SignUpResponse
import by.zenkevich_churun.findcell.server.internal.dao.auth.AuthorizationDao
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.PrisonerView
import javax.persistence.PersistenceException


class AuthorizationRepository(
    private val dao: AuthorizationDao ) {

    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisonerEntity = dao.getPrisoner(username, passwordHash)
        if(prisonerEntity != null) {
            val contacts = dao.getContacts(prisonerEntity.id)
            prisonerEntity.passwordHash = null
            val prisonerInstance = PrisonerView(prisonerEntity, contacts)
            return LogInResponse.Success(prisonerInstance)
        }

        val usernameRight = dao.checkUsername(username)
        if(!usernameRight) {
            return LogInResponse.WrongUsername
        }

        return LogInResponse.WrongPassword
    }


    fun signUp(
        username: String,
        passwordHash: ByteArray,
        initialName: String
    ): SignUpResponse {

        val id = try {
            dao.addPrisoner(username, passwordHash, initialName)
        } catch(exc: PersistenceException) {
            return SignUpResponse.UsernameTaken
        }

        val createdEntity = PrisonerEntity()
        createdEntity.id = id
        // Other fields don't have to be sent, because they can be filled in by the client.

        val createdPrisoner = PrisonerView(createdEntity, listOf())
        return SignUpResponse.Success(createdPrisoner)
    }
}