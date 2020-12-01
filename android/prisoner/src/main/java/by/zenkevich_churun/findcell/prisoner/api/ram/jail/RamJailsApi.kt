package by.zenkevich_churun.findcell.prisoner.api.ram.jail

import by.zenkevich_churun.findcell.core.api.JailsApi
import by.zenkevich_churun.findcell.core.entity.general.Jail
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RamJailsApi @Inject constructor(): JailsApi {
    private val random = Random()
    private val jails = listOf(
        RamJailEntity(17, "Окрестина ИВС", 24),
        RamJailEntity(11, "Окрестина ЦИП", 40),
        RamJailEntity(24, "Жодино",        210),
        RamJailEntity(16, "Барановичи",    150),
        RamJailEntity(29, "Могилёв",       180)
    )


    override fun jailsList(): List<Jail> {
        simulateNetworkRequest(900L, 1300L)
        return jails.map { jail ->
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
    }
}