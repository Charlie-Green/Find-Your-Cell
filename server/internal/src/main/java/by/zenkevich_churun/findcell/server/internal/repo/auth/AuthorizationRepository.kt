package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.server.internal.dao.auth.AuthorizationDao


// @Singleton
class AuthorizationRepository /* @Inject constructor() */ {

    // TODO: DI this
    private val dao = AuthorizationDao()


    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisoner = dao.getPrisoner(username, passwordHash)
        if(prisoner != null) {
            return LogInResponse.Success(prisoner.id)
        }

        val usernameRight = dao.checkUsername(username)
        if(!usernameRight) {
            return LogInResponse.WrongUsername
        }

        return LogInResponse.WrongPassword
    }
}