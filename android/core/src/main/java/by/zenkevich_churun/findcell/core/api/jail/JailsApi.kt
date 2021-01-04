package by.zenkevich_churun.findcell.core.api.jail

import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail


/** Fetches information about [Jail]s and [Cell]s. **/
interface JailsApi {
    fun jailsList(): List<Jail>
    fun cells(jailId: Int, jailName: String): List<Cell>
}