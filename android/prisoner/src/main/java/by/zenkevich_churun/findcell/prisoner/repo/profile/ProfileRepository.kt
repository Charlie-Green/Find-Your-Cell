package by.zenkevich_churun.findcell.prisoner.repo.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.api.auth.*
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.repo.common.PrisonerStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
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
            Log.w(LOGTAG, "Failed to log in: ${exc.javaClass.name}: ${exc.message}")
            LogInResponse.NetworkError
        }

        if(response is LogInResponse.Success) {
            store.submit(response.prisoner, passHash)
        }

        return response
    }

    fun signUp(username: String, password: String): SignUpResponse {
        val passHash = password.toByteArray(Charsets.UTF_16)
        val defaultName = appContext.getString(R.string.prisoner_default_name)

        val response = try {
            api.signUp(username, defaultName, passHash)
        } catch(exc: IOException) {
            Log.w(LOGTAG, "Failed to sign up: ${exc.javaClass.name}: ${exc.message}")
            SignUpResponse.NetworkError
        }

        if(response is SignUpResponse.Success) {
            store.submit(response.prisoner, passHash)
        }

        return response
    }

    fun saveDraft(draft: Prisoner) {
        store.prisonerLD.value?.also { extendedPrisoner ->
            store.submit(draft, extendedPrisoner.passwordHash)
        }
    }

    fun save(data: Prisoner, internet: Boolean) {
        if(!internet) {
            mldSaveResult.postValue(SavePrisonerResult.NoInternet)
            return
        }

        val passHash = store.prisonerLD.value?.passwordHash ?: return

        mldUnsavedChanges.postValue(false)
        try {
            api.update(data, passHash)

            store.submit(data, passHash)
            val deletedPositions = listOf<Int>()  // TODO
            mldSaveResult.postValue( SavePrisonerResult.Success(deletedPositions) )
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


    companion object {
        private const val LOGTAG = "FindCell-Prisoner"
    }
}