package by.sviazen.core.api.common

import by.sviazen.domain.entity.Prisoner


/** Throws by the API's methods if [Prisoner.id] and password hash don't match. **/
class WrongPasswordException: SecurityException()