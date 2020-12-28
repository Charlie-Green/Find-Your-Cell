package by.zenkevich_churun.findcell.entity.response

import by.zenkevich_churun.findcell.entity.entity.Prisoner


sealed class LogInResponse {
    class Success(
        val prisoner: Prisoner
    ): LogInResponse()

    object NetworkError: LogInResponse()
    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}