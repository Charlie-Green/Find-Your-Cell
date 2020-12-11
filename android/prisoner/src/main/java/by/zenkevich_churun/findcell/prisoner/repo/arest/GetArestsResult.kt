package by.zenkevich_churun.findcell.prisoner.repo.arest

import by.zenkevich_churun.findcell.core.entity.arest.Arest


sealed class GetArestsResult {

    class Success(
        val arests: List<Arest>
    ): GetArestsResult()

    object NotAuthorized: GetArestsResult()
    object NoInternet: GetArestsResult()
    object NetworkError: GetArestsResult()
}