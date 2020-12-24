package by.zenkevich_churun.findcell.server.internal.repo.auth


sealed class LogInResponse {

    class Success(
        val prisonerId: Int
    ): LogInResponse()

    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}