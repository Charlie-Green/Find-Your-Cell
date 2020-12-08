package by.zenkevich_churun.findcell.prisoner.api.ram.profile

import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.core.api.LogInResponse
import by.zenkevich_churun.findcell.core.api.ProfileApi
import by.zenkevich_churun.findcell.core.util.std.CollectionUtil
import by.zenkevich_churun.findcell.prisoner.api.ram.common.RamUserStorage
import java.io.IOException
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


/** Fake implementation of [ProfileApi], which stores data in RAM. **/
@Singleton
class RamProfileApi @Inject constructor(): ProfileApi {

    private val random = Random()


    override fun logIn(username: String, passwordHash: ByteArray): LogInResponse {
        simulateNetworkRequest(800L, 1500L)

        val prisoner = synchronized(RamUserStorage) {
            RamUserStorage.prisoners.find { prisoner ->
                prisoner.username == username &&
                prisoner.passwordHash.contentEquals(passwordHash)
            }
        }

        if(prisoner != null) {
            return LogInResponse.Success(prisoner)
        }

        val usernameExists = synchronized(RamUserStorage) {
            RamUserStorage.prisoners.find { prisoner ->
                prisoner.username == username
            } != null
        }

        return if(usernameExists) LogInResponse.WrongPassword
            else LogInResponse.WrongUsername
    }

    override fun signUp(
        username: String,
        name: String,
        passwordHash: ByteArray
    ): Int {

        simulateNetworkRequest(1000L, 1600L)

        synchronized(RamUserStorage) {
            val lastPrisoner = RamUserStorage.prisoners.maxBy {
                it.id
            }
            val id = lastPrisoner?.id?.plus(1)
                ?: Prisoner.INVALID_ID + 1

            val newPrisoner =
                PrisonerRamEntity(
                    id,
                    name,
                    username,
                    passwordHash,
                    listOf(),
                    ""
                )

            RamUserStorage.prisoners.add(newPrisoner)

            return id
        }
    }

    override fun update(prisoner: Prisoner, passwordHash: ByteArray) {
        simulateNetworkRequest(1000L, 1600L)

        synchronized(RamUserStorage) {
            val index = RamUserStorage.prisoners.indexOfFirst {
                it.id == prisoner.id && it.passwordHash.contentEquals(passwordHash)
            }
            if(index !in RamUserStorage.prisoners.indices) {
                throw IllegalArgumentException("Prisoner ID ${prisoner.id} not found")
            }

            RamUserStorage.prisoners[index] =
                PrisonerRamEntity(
                    prisoner.id,
                    prisoner.name,
                    RamUserStorage.prisoners[index].username,
                    passwordHash,
                    CollectionUtil.copyList(prisoner.contacts),
                    prisoner.info
                )
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
}