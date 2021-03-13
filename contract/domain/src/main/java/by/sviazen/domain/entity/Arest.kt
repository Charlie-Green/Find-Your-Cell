package by.sviazen.domain.entity

import by.sviazen.domain.util.CalendarUtil


/** Light information about 1 user arest,
  * that is the period between the first and the last day in jails. **/
class Arest(
    override val id: Int,
    startMillis: Long,
    endMillis: Long,
    val jails: List<Jail>
): LightArest() {

    override val start = CalendarUtil.midnight(startMillis)
    override val end: Long = CalendarUtil.midnight(endMillis)

    override val jailsCount: Int
        get() = jails.size

    override fun jailIdAt(index: Int): Int
        = jails[index].id


    companion object {
        const val INVALID_ID = 0


        /** Converts the given [LightArest] into an equivalent [Arest] instance
          * by substituting [Jail]s in place of their IDs.
          * @param la the [LightArest] to convert.
          * @param jails all [Jail]s. The jails related to this [LightArest]
          *        will be picked from this [Collection]. If a [Jail] is missing,
          *        it will be skipped and an appropriate warning will be logged.
          * @return [Arest] entity equivalent to [la], except for some [Jail]s
          *         that can be skipped if they are not present in [jails]. **/
        fun from(la: LightArest, jails: Collection<Jail>): Arest {
            if(la is Arest) {
                return la
            }

            val jailsMap = hashMapOf<Int, Jail>()
            for(jail in jails) {
                jailsMap[jail.id] = jail
            }

            val jailsList = mutableListOf<Jail>()
            for(index in 0 until la.jailsCount) {
                val jailId = la.jailIdAt(index)
                val jail = jailsMap[jailId]
                if(jail == null) {
                    println("Missing Jail for ID $jailId")
                    continue
                }

                jailsList.add(jail)
            }

            return Arest(
                la.id,
                la.start,
                la.end,
                jailsList
            )
        }
    }
}