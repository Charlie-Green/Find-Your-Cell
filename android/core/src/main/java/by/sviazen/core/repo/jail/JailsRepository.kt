package by.sviazen.core.repo.jail

import by.sviazen.domain.entity.Cell


interface JailsRepository {

    fun jailsList(internet: Boolean): GetJailsResult

    fun cell(
        jailId: Int,
        cellNumber: Short,
        internet: Boolean
    ): Cell?
}