package by.zenkevich_churun.findcell.entity.response

import by.zenkevich_churun.findcell.entity.entity.Prisoner


sealed class SignUpResponse {
    object NetworkError: SignUpResponse()
    object UsernameTaken: SignUpResponse()

    class Success(
        val prisoner: Prisoner
    ): SignUpResponse()
}