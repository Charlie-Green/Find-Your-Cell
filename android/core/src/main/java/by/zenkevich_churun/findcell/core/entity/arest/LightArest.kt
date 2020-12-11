package by.zenkevich_churun.findcell.core.entity.arest

import java.util.Calendar


/** Light version of [Arest] where full [Jail] entities are replaced with their IDs.
  * This is intended to save network traffic for fetching list of [Arest]s:
  * only [LightArest]s are fetched, then cached [Jail]s are found
  * in the local database and are put in place of IDs. **/
class LightArest(
    val id: Int,
    val start: Calendar,
    val end: Calendar,
    val jailIds: List<Int>
)