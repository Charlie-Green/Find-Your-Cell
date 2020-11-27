package by.zenkevich_churun.findcell.prisoner.repo.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.api.LogInResponse
import by.zenkevich_churun.findcell.core.api.ProfileApi
import by.zenkevich_churun.findcell.core.entity.general.Contact
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.prisoner.repo.common.PrisonerStorage
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileRepository @Inject constructor(
    private val api: ProfileApi,
    private val store: PrisonerStorage ) {

    private val mldUnsavedChanges = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val mldSaveResult = MutableLiveData<SavePrisonerResult>()


    val prisonerLD: LiveData<out Prisoner>
        get() = store.prisonerLD

    val unsavedChangesLD: LiveData<Boolean>
        get() = mldUnsavedChanges

    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = mldSaveResult


    fun logIn(username: String, password: String): LogInResponse {
        val passHash = password.toByteArray(Charsets.UTF_16)
        val response = try {
            api.logIn(username, passHash)
        } catch(exc: IOException) {
            LogInResponse.Error(exc)
        }

        if(response is LogInResponse.Success) {
            store.submit(response.prisoner, passHash)
        }

        return response
    }

    fun saveDraft(draft: Prisoner) {
        store.prisonerLD.value?.also { extendedPrisoner ->
            store.submit(draft, extendedPrisoner.passwordHash)
            mldUnsavedChanges.postValue(true)
        }
    }

    /** @return success **/
    fun save(data: Prisoner) {
        val passHash = store.prisonerLD.value?.passwordHash ?: return

        mldUnsavedChanges.postValue(false)
        try {
            api.update(data, passHash)

            store.submit(data, passHash)
            mldUnsavedChanges.postValue(false)
            mldSaveResult.postValue(SavePrisonerResult.SUCCESS)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to save ${Prisoner::class.java.simpleName}")
            mldUnsavedChanges.postValue(true)
            mldSaveResult.postValue(SavePrisonerResult.ERROR)
        }
    }

    fun notifySaveResultConsumed() {
        mldSaveResult.postValue(SavePrisonerResult.IGNORED)
    }


    companion object {
        private const val LOGTAG = "FindCell-Prisoner"
    }
}