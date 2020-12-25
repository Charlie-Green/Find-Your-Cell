package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.server.internal.dao.auth.AuthorizationDao


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
}