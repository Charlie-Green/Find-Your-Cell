package by.zenkevich_churun.findcell.core.api.auth

import by.zenkevich_churun.findcell.domain.contract.auth.LogInResponse
import by.zenkevich_churun.findcell.domain.contract.auth.SignUpResponse
import by.zenkevich_churun.findcell.domain.entity.Prisoner


/** Performs remote CRUD of [Prisoner] data.
  * All calls should be wrapped with try-catch. **/
interface ProfileApi {

    /** Gets information about the user.
      * @return any of [LogInResponse], except [LogInResponse.Error]. **/
    fun logIn(username: String, passwordHash: ByteArray): LogInResponse

    /** Creates a new [Prisoner] on the server.
      * @return any of [SignUpResponse], except [SignUpResponse.NetworkError].
      *         In case of [SignUpResponse.Success], the [Prisoner] within the instance
      *         contains the ID assigned to new user by the server. **/
    fun signUp(
        username: String,
        name: String,
        passwordHash: ByteArray
    ): SignUpResponse

    /** Updates user information.
      * [Prisoner.id] is used to identify the user.
      * Other fields within [prisoner] entity are the actual data
      * written to the server database.
      * Password hash is used to validate the client's right
      * to mutate information about this user. **/
    fun update(prisoner: Prisoner)
}