package by.sviazen.prisoner.ui.profile.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.repo.profile.ProfileRepository
import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner
import by.sviazen.core.repo.profile.SavePrisonerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    private val mldLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val medLdAddedContactTypes = addedContactTypesMediatorLD()
    private var lastPrisonerId = Prisoner.INVALID_ID


    val prisonerLD: LiveData<out Prisoner?>
        get() = repo.prisonerLD

    val addedContactTypesLD: LiveData< List<Contact.Type> >
        get() = medLdAddedContactTypes

    val unsavedChangesLD: LiveData<Boolean>
        get() = repo.unsavedChangesLD

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val saveResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD


    fun saveDraft(draft: Prisoner, contactTypes: List<Contact.Type>) {
        repo.saveDraft(draft)
        medLdAddedContactTypes.value = contactTypes
    }

    fun save(data: Prisoner) {
        if(mldLoading.value == true) {
            return
        }
        mldLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            repo.save(data, netTracker.isInternetAvailable)
            mldLoading.postValue(false)
        }
    }

    fun createContact(
        type: Contact.Type,
        existingContacts: Collection<Contact>
    ): Contact {
        return ProfileVMUtil.createContact(type, existingContacts)
    }


    fun notifySaveResultConsumed()
        = repo.notifySaveResultConsumed()

    fun notifyDataChanged()
        = repo.notifyDataChanged()


    private fun addedContactTypesMediatorLD(): MediatorLiveData< List<Contact.Type> > {
        val ld = MediatorLiveData< List<Contact.Type> >()

        ld.addSource(repo.prisonerLD) { prisoner ->
            // Publish new list only if the Prisoner changed
            // (or this is the first Prisoner emission).
            // In rest of cases, the list is updated on fly by the UI level.
            if(prisoner?.id != lastPrisonerId) {
                lastPrisonerId = prisoner?.id ?: Prisoner.INVALID_ID
                prisoner?.contacts?.also { contacts ->
                    ld.value = ProfileVMUtil.addedContactTypes(contacts)
                }

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