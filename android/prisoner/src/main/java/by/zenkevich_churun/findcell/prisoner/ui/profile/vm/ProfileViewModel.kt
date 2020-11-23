package by.zenkevich_churun.findcell.prisoner.ui.profile.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.repo.PrisonerRepository
import javax.inject.Inject


class ProfileViewModel @Inject constructor(
    private val repo: PrisonerRepository
): ViewModel() {

    val prisonerLD: LiveData<Prisoner>
        get() = repo.prisoneeLD

    fun saveDraft(draft: Prisoner) {
        repo.saveDraft(draft)
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ProfileViewModel {

            val fact = ProfileVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ProfileViewModel::class.java)
        }
    }
}