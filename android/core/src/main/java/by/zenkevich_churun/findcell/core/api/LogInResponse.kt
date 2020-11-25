package by.zenkevich_churun.findcell.core.api

import by.zenkevich_churun.findcell.core.entity.general.Prisoner


sealed class LogInResponse {
    class Success(
        val prisoner: Prisoner
    ): LogInResponse()

    class Error(
        val exc: Exception
    ): LogInResponse()

    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}