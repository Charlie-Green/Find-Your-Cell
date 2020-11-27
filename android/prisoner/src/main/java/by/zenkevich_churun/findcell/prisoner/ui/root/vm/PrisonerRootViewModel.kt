package by.zenkevich_churun.findcell.prisoner.ui.root.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import javax.inject.Inject


class PrisonerRootViewModel @Inject constructor(
    private val repo: ProfileRepository
): ViewModel() {

    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD

    fun notifySaveResultConsumed() {
        repo.notifySaveResultConsumed()
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): PrisonerRootViewModel {

            val fact = PrisonerRootVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(PrisonerRootViewModel::class.java)
        }
    }
}