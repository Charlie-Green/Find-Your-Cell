package by.sviazen.core.repo.profile

import by.sviazen.domain.contract.auth.AuthorizedPrisonerPojo
import by.sviazen.domain.contract.auth.SignUpResponse


sealed class SignUpResult {

    object WrongConfirmedPassword: SignUpResult()
    object NetworkError: SignUpResult()
    object UsernameTaken: SignUpResult()

    class Success(
        val prisoner: AuthorizedPrisonerPojo
    ): SignUpResult()


    companion object {
        fun fromServerResponse(
            response: SignUpResponse
        ) = when(response) {
            is SignUpResponse.Success -> SignUpResult.Success(response.prisoner)
            is SignUpResponse.UsernameTaken -> SignUpResult.UsernameTaken
        }

        fun networkError() = SignUpResult.NetworkError

        fun wrongConfirmedPassword() = SignUpResult.WrongConfirmedPassword
    }
}