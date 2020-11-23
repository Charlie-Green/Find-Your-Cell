package by.zenkevich_churun.findcell.prisoner.ui.profile.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.Prisoner
import by.zenkevich_churun.findcell.prisoner.repo.PrisonerRepository
import by.zenkevich_churun.findcell.prisoner.repo.SavePrisonerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor(
    private val repo: PrisonerRepository
): ViewModel() {

    private val mldLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val mldSaveResult = MutableLiveData<SavePrisonerResult>()


    val prisonerLD: LiveData<Prisoner>
        get() = repo.prisoneeLD

    val unsavedChangesLD: LiveData<Boolean>
        get() = repo.unsavedChangesLD

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val saveResultLD: LiveData<SavePrisonerResult>
        get() = mldSaveResult


    fun saveDraft(draft: Prisoner) {
        repo.saveDraft(draft)
    }

    fun save(data: Prisoner) {
        if(mldLoading.value == true) {
            return
        }
        mldLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.save(data)
            mldSaveResult.postValue(result)
            mldLoading.postValue(false)
        }
    }

    fun notifySaveResultConsumed() {
        mldSaveResult.value = SavePrisonerResult.IGNORED
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