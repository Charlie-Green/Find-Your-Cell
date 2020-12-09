package by.zenkevich_churun.findcell.prisoner.repo.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerStorage @Inject constructor() {
    private val mldPrisoner = MutableLiveData<ExtendedPrisoner>()

    val prisonerLD: LiveData<ExtendedPrisoner>
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
}