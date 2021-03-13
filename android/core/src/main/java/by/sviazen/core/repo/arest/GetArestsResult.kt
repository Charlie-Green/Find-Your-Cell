package by.sviazen.core.repo.arest


sealed class GetArestsResult {

    object Success: GetArestsResult()
    object NotAuthorized: GetArestsResult()
    object NetworkError: GetArestsResult()
}