package by.zenkevich_churun.findcell.domain.contract.auth

import by.zenkevich_churun.findcell.domain.entity.Prisoner


sealed class SignUpResponse {
    object NetworkError: SignUpResponse()
    object UsernameTaken: SignUpResponse()

    class Success(
        val prisoner: AuthorizedPrisonerPojo
    ): SignUpResponse()
}