package by.zenkevich_churun.findcell.core.api

import by.zenkevich_churun.findcell.core.entity.general.Prisoner


/** Throws by the API's methods if [Prisoner.id] and password hash don't match. **/
class WrongPasswordException: SecurityException()