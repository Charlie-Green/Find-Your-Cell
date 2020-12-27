package by.zenkevich_churun.findcell.contract.prisoner.entity

import by.zenkevich_churun.findcell.core.entity.general.Jail
import java.util.Calendar


/** Light version of [Arest] where full [Jail] entities are replaced with their IDs.
  * This is intended to save network traffic for fetching list of [Arest]s:
  * only [LightArest]s are fetched, then cached [Jail]s are found
  * in the local database and are put in place of IDs. **/
abstract class LightArest {

    /** Database identifier. Assigned by the server. **/
    abstract val id: Int

    /** The first day in a [Jail]. **/
    abstract val start: Calendar

    /** The last day in a [Jail]. **/
    abstract val end: Calendar

    /** The number of distinct [Jail]s
      * the [Prisoner] was imprisoned into during this [Arest]. **/
    abstract val jailsCount: Int

    /** This method allows iterating over a collection of IDs
      * of the [Jail]s the [Prisoner] was imprisoned into during this [Arest]. **/
    abstract fun jailIdAt(index: Int): Int
}