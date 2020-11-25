package by.zenkevich_churun.findcell.prisoner.ui.profile.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Contact
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
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
    private val medLdAddedContactTypes = addedContactTypesMediatorLD()
    private var lastPrisonerId = Prisoner.INVALID_ID


    val prisonerLD: LiveData<Prisoner>
        get() = repo.prisonerLD

    val addedContactTypesLD: LiveData< MutableList<Contact.Type> >
        get() = medLdAddedContactTypes

    val unsavedChangesLD: LiveData<Boolean>
        get() = repo.unsavedChangesLD

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val saveResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD


    fun saveDraft(draft: Prisoner) {
        repo.saveDraft(draft)
    }

    fun save(data: Prisoner) {
        if(mldLoading.value == true) {
            return
        }
        mldLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            repo.save(data)
            mldLoading.postValue(false)
        }
    }

    fun notifySaveResultConsumed() {
        repo.notifySaveResultConsumed()
    }

    fun createContact(
        type: Contact.Type,
        existingContacts: Collection<Contact>
    ): Contact {
        return ProfileVMUtil.createContact(type, existingContacts)
    }


    private fun addedContactTypesMediatorLD(): MediatorLiveData< MutableList<Contact.Type> > {
        val ld = MediatorLiveData< MutableList<Contact.Type> >()

        ld.addSource(repo.prisonerLD) { prisoner ->
            // Publish new list only if the Prisoner changed
            // (or this is the first Prisoner emission).
            // In rest of cases, the list is updated on fly by the UI level.
            if(prisoner.id != lastPrisonerId) {
                lastPrisonerId = prisoner.id
                ld.value = ProfileVMUtil.addedContactTypes(prisoner.contacts)
            }
        }

        return ld
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