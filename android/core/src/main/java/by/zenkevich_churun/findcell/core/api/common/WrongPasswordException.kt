package by.zenkevich_churun.findcell.core.api.common

import by.zenkevich_churun.findcell.entity.entity.Prisoner


/** Throws by the API's methods if [Prisoner.id] and password hash don't match. **/
class WrongPasswordException: SecurityException()