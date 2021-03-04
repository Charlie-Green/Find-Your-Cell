package by.sviazen.core.api.jail

import by.sviazen.domain.entity.Cell
import by.sviazen.domain.entity.Jail


/** Fetches information about [Jail]s and [Cell]s. **/
interface JailsApi {
    fun jailsList(): List<Jail>
    fun cells(jailId: Int, jailName: String): List<Cell>
}