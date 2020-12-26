package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner


sealed class LogInResponse {

    class Success(
        val prisoner: Prisoner
    ): LogInResponse()

    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}