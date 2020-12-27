package by.zenkevich_churun.findcell.core.api.auth

import by.zenkevich_churun.findcell.entity.Prisoner


sealed class SignUpResponse {
    object NetworkError: SignUpResponse()
    object UsernameExists: SignUpResponse()

    class Success(
        val prisoner: Prisoner
    ): SignUpResponse()
}