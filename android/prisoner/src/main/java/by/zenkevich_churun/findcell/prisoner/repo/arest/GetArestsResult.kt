package by.zenkevich_churun.findcell.prisoner.repo.arest


sealed class GetArestsResult {

    object Success: GetArestsResult()
    object NotAuthorized: GetArestsResult()
    object NetworkError: GetArestsResult()
}