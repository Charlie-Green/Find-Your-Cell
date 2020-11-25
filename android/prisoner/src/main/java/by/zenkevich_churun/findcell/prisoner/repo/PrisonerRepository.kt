package by.zenkevich_churun.findcell.prisoner.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.api.LogInResponse
import by.zenkevich_churun.findcell.core.api.PrisonerApi
import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerRepository @Inject constructor(
    private val api: PrisonerApi ) {

    private val mldPrisoner = MutableLiveData<Prisoner>().apply {
        value = object: Prisoner() {
            override val id: Int
                get() = Prisoner.INVALID_ID + 1

            override val name: String
                get() = "Simon"

            override val contacts: List<Contact>
                get() = listOf(
                    Contact.Phone("+1 456 1234567"),
                    Contact.Skype("live:simon_fake")
                )

            override val info: String
                get() = "Fake User"
        }
    }

    private val mldUnsavedChanges = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val mldSaveResult = MutableLiveData<SavePrisonerResult>()

    private var passwordHash: ByteArray? = /* null */ "pass".toByteArray(Charsets.UTF_16)


    val prisonerLD: LiveData<Prisoner>
        get() = mldPrisoner

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
            mldPrisoner.postValue(response.prisoner)
        }

        return response
    }

    fun saveDraft(draft: Prisoner) {
        mldPrisoner.postValue(draft)
        mldUnsavedChanges.postValue(true)
    }

    /** @return success **/
    fun save(data: Prisoner) {
        val passHash = passwordHash ?: return

        mldUnsavedChanges.postValue(false)
        try {
            api.update(data, passHash)

            mldPrisoner.postValue(data)
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