package by.zenkevich_churun.findcell.server.protocol.controller.auth

import by.zenkevich_churun.findcell.server.internal.repo.auth.LogInResponse


internal object AuthorizationMapping {

    fun encode(
        response: LogInResponse
    ): String = when(response) {

        is LogInResponse.Success -> "${response.prisonerId}"
        is LogInResponse.WrongUsername -> "U"
        is LogInResponse.WrongPassword -> "P"
    }
}