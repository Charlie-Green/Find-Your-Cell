package by.zenkevich_churun.findcell.core.entity.arest

import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.core.util.std.CalendarUtil
import java.util.Calendar


/** Light information about 1 user arest,
  * that is the period between the first and the last day in jails. **/
class Arest(

    /** Database identifier. Assigned by the server. **/
    val id: Int,

    /** The first day in a [Jail]. **/
    val start: Calendar,

    /** The last day in a [Jail]. **/
    val end: Calendar,

    /** The full list of [Jail]s the user has been to during this [Arest]. **/
    val jails: List<Jail> ) {

    init {
        CalendarUtil.setToMidnight(start)
        CalendarUtil.setToMidnight(end)
    }


    companion object {

        const val INVALID_ID = 0


        /** @return [Arest] entity obtained from the given [LightArest] entity
          *         by substituting [Jail]s from the [Collection] instead of [Jail] IDs.
          *         Null is returned if a [Jail] couldn't be found for an ID. **/
        fun from(
            la: LightArest,
            availableJails: Collection<Jail>
        ): Arest? {

            val jails = mutableListOf<Jail>()
            for(id in la.jailIds) {

                val jail = availableJails.find { j ->
                    j.id == id
                }

                if(jail == null) {
                    return null
                }

                jails.add(jail)
            }

            return Arest(
                la.id,
                la.start,
                la.end,
                jails
            )
        }
    }
}