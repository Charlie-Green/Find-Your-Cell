package by.zenkevich_churun.findcell.prisoner.repo.jail

import by.zenkevich_churun.findcell.entity.Jail


sealed class GetJailsResult {
    class Success(
        val jails: List<Jail>
    ): GetJailsResult()

    object FirstTimeError: GetJailsResult()
    object FirstTimeNeedInternet: GetJailsResult()
}