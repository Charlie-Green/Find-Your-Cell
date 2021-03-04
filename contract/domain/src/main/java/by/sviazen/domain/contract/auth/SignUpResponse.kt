package by.sviazen.domain.contract.auth

import by.sviazen.domain.entity.Prisoner


sealed class SignUpResponse {
    object NetworkError: SignUpResponse()
    object UsernameTaken: SignUpResponse()

    class Success(
        val prisoner: AuthorizedPrisonerPojo
    ): SignUpResponse()
}