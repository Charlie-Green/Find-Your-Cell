package by.zenkevich_churun.findcell.core.api

import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Jail


/** Fetches information about [Jail]s and [Cell]s. **/
interface JailsApi {
    fun jailsList(): List<Jail>
}