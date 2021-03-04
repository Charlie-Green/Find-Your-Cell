package by.sviazen.prisoner.repo.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.sviazen.core.api.auth.*
import by.sviazen.prisoner.R
import by.sviazen.core.common.prisoner.PrisonerStorage
import by.sviazen.core.injected.common.Hasher
import by.sviazen.core.injected.sync.AutomaticSyncManager
import by.sviazen.domain.contract.auth.LogInResponse
import by.sviazen.domain.contract.auth.SignUpResponse
import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner
import by.sviazen.domain.simpleentity.SimplePrisoner
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: ProfileApi,
    private val prisonerStore: PrisonerStorage,
    private val autoSyncMan: AutomaticSyncManager,
    private val hasher: Hasher ) {

    private val authStore = AuthorizationMetadataStorage(appContext)

    private val mldUnsavedChanges = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val mldSaveResult = MutableLiveData<SavePrisonerResult>()


    val prisonerLD: LiveData<out Prisoner?>
        get() = prisonerStore.prisonerLD

    val unsavedChangesLD: LiveData<Boolean>
        get() = mldUnsavedChanges

    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = mldSaveResult


    fun logIn(username: String, password: String): LogInResponse {
        val passHash = hasher.hash(password)
        val response = try {
            api.logIn(username, passHash)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to log in: ${exc.javaClass.name}: ${exc.message}")
            LogInResponse.NetworkError
        }

        if(response is LogInResponse.Success) {
            applyLogInSuccess(response, passHash)
        }

        return response
    }

    fun signUp(
        username: String,
        password: String
    ): SignUpResponse {

        val passHash = hasher.hash(password)
        val defaultName = appContext.getString(R.string.prisoner_default_name)

        val response = try {
            api.signUp(username, defaultName, passHash)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to sign up: ${exc.javaClass.name}: ${exc.message}")
            SignUpResponse.NetworkError
        }

        if(response is SignUpResponse.Success) {
            prisonerStore.submit(response.prisoner, passHash)
        }

        return response
    }

    fun logOut() {
        prisonerStore.clear()
        autoSyncMan.clearCoPrisonersCache()
        autoSyncMan.set(false)
    }


    fun saveDraft(draft: Prisoner) {
        val original = prisonerLD.value ?: return
        val draftWithPassword = SimplePrisoner(
            original.id,
            original.username,
            original.passwordHash,
            draft.name,
            draft.info,
            draft.contacts
        )

        prisonerStore.submit(draftWithPassword)
    }

    fun save(data: Prisoner, internet: Boolean) {
        if(!internet) {
            mldSaveResult.postValue(SavePrisonerResult.NoInternet)
            return
        }

        val deletedPositions = mutableListOf<Int>()
        val clearData = removeBlankContacts(data, deletedPositions)

        mldUnsavedChanges.postValue(false)
        try {
            api.update(clearData)
            val result = SavePrisonerResult.Success(deletedPositions)
            mldSaveResult.postValue(result)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to save ${Prisoner::class.java.simpleName}")
            mldUnsavedChanges.postValue(true)
            mldSaveResult.postValue(SavePrisonerResult.Error)
        }
    }


    fun notifySaveResultConsumed() {
        mldSaveResult.postValue(SavePrisonerResult.Idle)
    }

    fun notifyDataChanged() {
        mldUnsavedChanges.postValue(true)
    }

    fun withdrawUnsavedChanges() {
        mldUnsavedChanges.value = false
    }


    private fun removeBlankContacts(
        data: Prisoner,
        positions: MutableList<Int>
    ): Prisoner {

        val clearedContacts = mutableListOf<Contact>()
        for(j in data.contacts.indices) {
            val contact = data.contacts[j]

            if(contact.data.isBlank()) {
                positions.add(j - positions.size)
            } else {
                clearedContacts.add(contact)
            }
        }

        return SimplePrisoner(
            data.id,
            data.username,
            data.passwordHash ?: throw NullPointerException("Password must be present"),
            data.name,
            data.info,
            clearedContacts,
        )
    }

    private fun applyLogInSuccess(response: LogInResponse.Success, passHash: ByteArray) {
        prisonerStore.submit(response.prisoner, passHash)

        if(authStore.lastPrisonerId != response.prisoner.id) {
            authStore.lastPrisonerId = response.prisoner.id
            autoSyncMan.clearCoPrisonersCache()
        }
        autoSyncMan.set(true)
    }


    companion object {
        private const val LOGTAG = "FindCell-Prisoner"
    }
}