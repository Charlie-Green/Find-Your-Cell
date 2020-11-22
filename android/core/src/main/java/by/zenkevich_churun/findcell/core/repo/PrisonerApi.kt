package by.zenkevich_churun.findcell.core.repo

import by.zenkevich_churun.findcell.core.entity.Prisoner


/** Performs remote CRUD of [Prisoner] data **/
interface PrisonerApi {

    /** @return - [Prisoner] entity with full information about the user,
      *         if log in was successful.
      *         - null, if such combination of username and password hash doesn't exist. **/
    fun logIn(username: String, passwordHash: ByteArray): Prisoner?

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