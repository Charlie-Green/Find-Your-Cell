package by.zenkevich_churun.findcell.prisoner.repo.arest

import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.entity.LightArest
import java.util.*


/** Keeps current list of [Arest]s in RAM.
  * Provides methods to update cache after changes were made remotely.
  * Thread-safe. **/
internal object ArestsCache {

    private val arests: MutableList<Arest> = mutableListOf()


    val cachedList: List<Arest>
        get() = synchronized(arests) {
            arests
        }


    fun submit(list: List<Arest>) {
        synchronized(arests) {
            arests.clear()
            arests.addAll(list)
            arests.sortWith(ArestsComparator)
        }
    }


    /** @return index in the list where the [Arest] was inserted. **/
    fun insert(newArest: Arest): Int {
        synchronized(arests) {
            val position = positionFor(newArest)
            arests.add(position, newArest)
            return position
        }
    }

    /** @return pair of old index and new index **/
    fun update(updatedArest: Arest): Pair<Int, Int> {
        synchronized(arests) {
            val oldPosition = findArest(updatedArest.id)
            val newPosition = positionFor(updatedArest)
            arests.removeAt(oldPosition)
            arests.add(newPosition, updatedArest)
            return oldPosition to newPosition
        }
    }


    private fun findArest(id: Int): Int {
        val position = arests.indexOfFirst { existingArest ->
            existingArest.id == id
        }

        if(position !in arests.indices) {
            throw IllegalArgumentException("No Arest with ID $id")
        }
        return position
    }

    private fun positionFor(newArest: Arest): Int {
        val position = arests.indexOfFirst { existingArest ->
            ArestsComparator.compare(newArest, existingArest) < 0
        }

        if(position in arests.indices) {
            return position
        }
        return arests.size
    }


    private object ArestsComparator: Comparator<LightArest> {

        override fun compare(a: LightArest, b: LightArest): Int {
            var code = compareCalendars(a.start, b.start)
            if(code == 0) {
                code = compareCalendars(a.end, b.end)
            }

            return code
        }


        private fun compareCalendars(a: Calendar, b: Calendar): Int {
            val millisA = a.timeInMillis
            val millisB = b.timeInMillis

            if(millisA < millisB) {
                return -1
            }
            if(millisA > millisB) {
                return 1
            }

            return 0
        }
    }
}