package by.zenkevich_churun.findcell.core.common.prisoner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerStorage @Inject constructor() {
    private val mldPrisoner = MutableLiveData<ExtendedPrisoner?>().apply {
        // TODO: CharlieDebug:
        value = ExtendedPrisoner(
            1,
            "Романчик",
            listOf(
                object: Contact() {
                    override val type: Type = Type.VK
                    override val data: String = "vk.com/myvk"
                },

                object: Contact() {
                    override val type: Type = Type.TELEGRAM
                    override val data: String = "t.me/mytelega"
                },
            ),
            "This is a fake test user. Remove it from PrisonerStorage class when not needed.",
            "111".toByteArray(Charsets.UTF_8)
        )
    }

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