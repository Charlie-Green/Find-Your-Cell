package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.server.internal.dao.auth.AuthorizationDao
import javax.persistence.PersistenceException


class AuthorizationRepository(
    private val dao: AuthorizationDao ) {

    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisoner = dao.getPrisoner(username, passwordHash)
        if(prisoner != null) {
            val contacts = dao.getContacts(prisoner.id)
            return LogInResponse.Success(prisoner, contacts)
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

        try {
            val id = dao.addPrisoner(username, passwordHash, initialName)
            return SignUpResponse.Success(id)
        } catch(exc: PersistenceException) {
            return SignUpResponse.UsernameTaken
        }
    }
}