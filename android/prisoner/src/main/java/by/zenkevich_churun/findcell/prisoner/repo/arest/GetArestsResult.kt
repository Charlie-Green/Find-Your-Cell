package by.zenkevich_churun.findcell.prisoner.repo.arest

import by.zenkevich_churun.findcell.entity.Arest


sealed class GetArestsResult {

    class Success(
        val arests: List<Arest>
    ): GetArestsResult()

    object NotAuthorized: GetArestsResult()
    object NetworkError: GetArestsResult()
}