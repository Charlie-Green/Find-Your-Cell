package by.sviazen.prisoner.repo.arest


sealed class GetArestsResult {

    object Success: GetArestsResult()
    object NotAuthorized: GetArestsResult()
    object NetworkError: GetArestsResult()
}