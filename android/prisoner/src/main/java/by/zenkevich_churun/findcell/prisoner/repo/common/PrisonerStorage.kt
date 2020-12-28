package by.zenkevich_churun.findcell.prisoner.repo.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerStorage @Inject constructor() {
    private val mldPrisoner = MutableLiveData<ExtendedPrisoner?>()/*.apply {
        // TODO: Remove when Log In is implemented.
        value = ExtendedPrisoner(
            Prisoner.INVALID_ID + 1,
            "Charlie",
            listOf(
                Contact.Telegram("@my_teleg"),
                Contact.VK("vk.com/myvk")
            ),
            "This is a fake test user.",
            "pass".toByteArray(Charsets.UTF_16)
        )
    }*/

    val prisonerLD: LiveData<ExtendedPrisoner?>
        get() = mldPrisoner

    fun submit(prisoner: Prisoner, passwordHash: ByteArray) {
        val ep = ExtendedPrisoner(
            prisoner.id,
            prisoner.name,
            prisoner.contacts,
            prisoner.info,
            passwordHash
        )

        mldPrisoner.postValue(ep)
    }

    fun clear() {
        mldPrisoner.postValue(null)
    }
}