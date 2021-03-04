package by.sviazen.prisoner.repo.arest

import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Jail
import by.sviazen.domain.entity.LightArest


internal object ArestsMapper {

    /** Checks if all [Jail]s required to map
      * the given [LightArest]s into [Arest]s are present.  **/
    fun areAllJailsPresent(
        arests: List<LightArest>,
        jails: List<Jail>
    ): Boolean {

        val jailsSet = hashSetOf<Int>()
        for(j in jails) {
            jailsSet.add(j.id)
        }

        for(a in arests) {
            for(index in 0 until a.jailsCount) {
                val j = a.jailIdAt(index)
                if(!jailsSet.contains(j)) {
                    return false
                }
            }
        }

        return true
    }


    fun map(
        lightArests: Collection<LightArest>,
        jails: Collection<Jail>
    ): List<Arest> {

        return lightArests.map { la ->
            Arest.from(la, jails)
        }
    }
}