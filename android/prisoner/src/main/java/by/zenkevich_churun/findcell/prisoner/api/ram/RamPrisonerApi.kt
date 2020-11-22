package by.zenkevich_churun.findcell.prisoner.api.ram

import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.core.repo.PrisonerApi
import java.util.Random


/** Fake implementation of [PrisonerApi], which stores data in RAM. **/
class RamPrisonerApi: PrisonerApi {

    private val random = Random()
    private val prisoners = mutableListOf(
        PrisonerRamEntity(
            Prisoner.INVALID_ID + 1,
            "Charlie",
            "charlz123",
            "pass".toByteArray(Charsets.UTF_16),
            listOf(
                Contact.Phone("+1 23 456789"),
                Contact.Telegram("@my_telegram")
            ),
            "This is a fake RAM user."
        )
    )


    override fun logIn(username: String, passwordHash: ByteArray): Prisoner? {
        simulateNetworkRequest(800L, 1500L)

        synchronized(prisoners) {
            return prisoners.find { prisoner ->
                prisoner.username == username &&
                    prisoner.passwordHash.contentEquals(passwordHash)
            }
        }
    }

    override fun signUp(
        username: String,
        name: String,
        passwordHash: ByteArray
    ): Int {

        simulateNetworkRequest(1000L, 1600L)

        synchronized(prisoners) {
            val lastPrisoner = prisoners.maxBy {
                it.id
            }
            val id = lastPrisoner?.id?.plus(1)
                ?: Prisoner.INVALID_ID + 1

            val newPrisoner = PrisonerRamEntity(
                id,
                name,
                username,
                passwordHash,
                listOf(),
                ""
            )

            prisoners.add(newPrisoner)

            return id
        }
    }

    override fun update(prisoner: Prisoner, passwordHash: ByteArray) {
        simulateNetworkRequest(1000L, 1600L)

        synchronized(prisoners) {
            val index = prisoners.indexOfFirst { it.id == prisoner.id }
            if(index !in prisoners.indices) {
                throw IllegalArgumentException("Prisoner ID ${prisoner.id} not found")
            }
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