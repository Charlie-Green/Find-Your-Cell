package by.zenkevich_churun.findcell.core.api.auth

import by.zenkevich_churun.findcell.entity.Prisoner


sealed class LogInResponse {
    class Success(
        val prisoner: Prisoner
    ): LogInResponse()

    object NetworkError: LogInResponse()
    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}