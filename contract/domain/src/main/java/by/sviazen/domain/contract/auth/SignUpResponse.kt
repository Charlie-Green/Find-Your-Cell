package by.sviazen.domain.contract.auth


sealed class SignUpResponse {
    object UsernameTaken: SignUpResponse()

    class Success(
        val prisoner: AuthorizedPrisonerPojo
    ): SignUpResponse()
}