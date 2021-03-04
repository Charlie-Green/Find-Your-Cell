package by.sviazen.prisoner.repo.jail

import by.sviazen.domain.entity.Jail


sealed class GetJailsResult {
    class Success(
        val jails: List<Jail>
    ): GetJailsResult()

    object FirstTimeError: GetJailsResult()
    object FirstTimeNeedInternet: GetJailsResult()
}