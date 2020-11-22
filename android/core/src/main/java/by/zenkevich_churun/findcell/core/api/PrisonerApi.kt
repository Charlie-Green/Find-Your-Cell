package by.zenkevich_churun.findcell.core.api

import by.zenkevich_churun.findcell.core.entity.Prisoner


/** Performs remote CRUD of [Prisoner] data.
  * All calls should be wrapped with try-catch. **/
interface PrisonerApi {

    /** Gets information about the user. **/
    fun logIn(username: String, passwordHash: ByteArray): LogInResponse

    /** Creates a new [Prisoner] on the server.
      * @return id the server assigned to the [Prisoner],
      *         or [Prisoner.INVALID_ID] if username already exists. **/
    fun signUp(
        username: String,
        name: String,
        passwordHash: ByteArray
    ): Int

    /** Updates user information.
      * [Prisoner.id] is used to identify the user.
      * Other fields within [prisoner] entity are the actual data
      * written to the server database.
      * Password hash is used to validate the client's right
      * to mutate information about this user. **/
    fun update(prisoner: Prisoner, passwordHash: ByteArray)


    /** Throws by the API's methods if [Prisoner.id] and password hash don't match. **/
    class WrongPasswordException: SecurityException()
}