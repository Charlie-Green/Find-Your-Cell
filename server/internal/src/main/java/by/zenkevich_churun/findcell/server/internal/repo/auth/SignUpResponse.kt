package by.zenkevich_churun.findcell.server.internal.repo.auth


sealed class SignUpResponse {

    class Success(
        val prisonerId: Int
    ): SignUpResponse()

    object UsernameTaken: SignUpResponse()
}