package by.zenkevich_churun.findcell.prisoner.api.ram.arest

import by.zenkevich_churun.findcell.core.api.arest.ArestsApi
import by.zenkevich_churun.findcell.core.api.arest.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.core.entity.arest.LightArest
import by.zenkevich_churun.findcell.prisoner.api.ram.common.RamJailsStorage
import by.zenkevich_churun.findcell.prisoner.api.ram.common.RamUserStorage
import java.io.IOException
import java.util.Calendar
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RamArestsApi @Inject constructor(): ArestsApi {

    private val arests = mutableListOf(

        LightArest(
            17,
            cal( 5, Calendar.SEPTEMBER, 2020),
            cal(15,  Calendar.NOVEMBER, 2020),
            listOf(
                jailId("Окрестина ИВС"),
                jailId("Окрестина ЦИП"),
                jailId("Жодино")
            )
        ),

        LightArest(
            13,
            cal( 8, 11, 2020),
            cal(18, 11, 2020),
            listOf(
                jailId("Жодино")
            )
        )
    )

    private val random = Random()


    override fun get(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<LightArest> {

        simulateNetworkRequest(1400L, 2000L)
        RamUserStorage.validate(prisonerId, passwordHash)

        synchronized(arests) {
            return arests.map { a ->
                copyOf(a)
            }
        }
    }

    override fun create(
        prisonerId: Int,
        passwordHash: ByteArray,
        start: Calendar,
        end: Calendar
    ): CreateOrUpdateArestResponse {

        simulateNetworkRequest(900L, 1400L)
        RamUserStorage.validate(prisonerId, passwordHash)

        synchronized(arests) {
            val intersected = findIntersectedArest(start, end)
            if(intersected != null) {
                return CreateOrUpdateArestResponse.ArestsIntersect(intersected.id)
            }

            val newArest = LightArest(
                nextId,
                start.clone() as Calendar,
                end.clone() as Calendar,
                listOf()
            )
            arests.add(newArest)

            return CreateOrUpdateArestResponse.Success(newArest.id)
        }
    }

    override fun update(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int,
        newStart: Calendar,
        newEnd: Calendar
    ): CreateOrUpdateArestResponse {

        simulateNetworkRequest(900L, 1400L)
        RamUserStorage.validate(prisonerId, passwordHash)

        synchronized(arests) {
            val intersected = findIntersectedArest(newStart, newEnd)
            if(intersected != null) {
                return CreateOrUpdateArestResponse.ArestsIntersect(intersected.id)
            }

            val index = indexById(id)

            arests[index] = LightArest(
                id,
                newStart.clone() as Calendar,
                newEnd.clone() as Calendar,
                arests[index].jailIds.map { id -> id }  // Clone
            )

            return CreateOrUpdateArestResponse.Success(id)
        }
    }

    override fun delete(
        prisonerId: Int,
        passwordHash: ByteArray,
        id: Int ) {

        simulateNetworkRequest(700L, 1100L)
        RamUserStorage.validate(prisonerId, passwordHash)

        synchronized(arests) {
            val index = indexById(id)
            arests.removeAt(index)
        }
    }


    private val nextId: Int
        get() {
            val maxId = arests.maxBy { arest ->
                arest.id
            }?.id ?: 0
            return maxId + 1
        }


    private fun simulateNetworkRequest(minTime: Long, maxTime: Long) {
        val delta = (maxTime - minTime).toInt()
        val time = minTime + random.nextInt(delta)
        try {
            Thread.sleep(time)
        } catch(exc: InterruptedException) {
            // Empty.
        }
    }


    private fun cal(date: Int, month: Int, year: Int): Calendar {
        return Calendar.getInstance().apply { set(year, month, date) }
    }

    private fun jailId(jailName: String): Int {
        return RamJailsStorage.jails.find { jail ->
            jail.name == jailName
        }!!.id
    }

    private fun copyOf(arest: LightArest): LightArest {
        return LightArest(
            arest.id,
            arest.start.clone() as Calendar,
            arest.end.clone() as Calendar,
            arest.jailIds.map { id -> id }  // Clone the list.
        )
    }

    private fun findIntersectedArest(start: Calendar, end: Calendar): LightArest? {
        return arests.find { arest ->
            start > arest.end || end < arest.start
        }
    }

    private fun indexById(id: Int): Int {
        val index = arests.indexOfFirst { arest ->
            arest.id == id
        }

        if(index !in arests.indices) {
            throw IllegalArgumentException("No Arest with ID $id")
        }

        return index
    }
}