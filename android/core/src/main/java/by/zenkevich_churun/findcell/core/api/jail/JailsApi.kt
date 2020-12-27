package by.zenkevich_churun.findcell.core.api.jail

import by.zenkevich_churun.findcell.entity.Cell
import by.zenkevich_churun.findcell.entity.Jail


/** Fetches information about [Jail]s and [Cell]s. **/
interface JailsApi {
    fun jailsList(): List<Jail>
    fun cell(jailId: Int, cellNumber: Short): Cell
}