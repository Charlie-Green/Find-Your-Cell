package by.zenkevich_churun.findcell.prisoner.api.ram.jail

import by.zenkevich_churun.findcell.core.api.JailsApi
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.prisoner.api.ram.common.RamJailsStorage
import java.io.IOException
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RamJailsApi @Inject constructor(): JailsApi {
    private val random = Random()


    override fun jailsList(): List<Jail> {
        simulateNetworkRequest(900L, 1300L)
        return RamJailsStorage.jails.map { jail ->
            jail.copy()
        }
    }


    private fun simulateNetworkRequest(minTime: Long, maxTime: Long) {
        val delta = (maxTime - minTime).toInt()
        val time = minTime + random.nextInt(delta)
        try {
            Thread.sleep(time)
        } catch(exc: InterruptedException) {
            // Empty.
        }

        // TODO: CharlieDebug:
        throw IOException("Simulated Error")
    }


    override fun cell(jailId: Int, cellNumber: Short): Cell {
        val jail = RamJailsStorage.jails.find { j ->
            j.id == jailId
        }
        jail ?: throw IllegalArgumentException("No Jail with ID $jailId")

        return RamCellEntity(jailId, jail.name, cellNumber, 8)
    }
}