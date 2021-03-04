package by.sviazen.domain.contract.auth


sealed class LogInResponse {
    class Success(
        val prisoner: AuthorizedPrisonerPojo
    ): LogInResponse()

    object NetworkError: LogInResponse()
    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}