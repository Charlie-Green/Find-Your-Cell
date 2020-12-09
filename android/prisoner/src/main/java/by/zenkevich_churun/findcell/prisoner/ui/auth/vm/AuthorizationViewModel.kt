package by.zenkevich_churun.findcell.prisoner.ui.auth.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.api.LogInResponse
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.ui.auth.model.AuthorizationState
import by.zenkevich_churun.findcell.prisoner.ui.auth.model.PrisonerCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthorizationViewModel @Inject constructor(
    private val repo: ProfileRepository
): ViewModel() {

    private val mldState = MutableLiveData<AuthorizationState>()

    val stateLD: LiveData<AuthorizationState>
        get() = mldState


    fun logIn(username: String, password: String) {
        authorize(username, password) {
            when(val response = repo.logIn(username, password)) {
                is LogInResponse.WrongUsername -> AuthorizationState.USERNAME_NOT_EXIST
                is LogInResponse.WrongPassword -> AuthorizationState.PASSWORD_NOT_MATCH
                is LogInResponse.Error         -> AuthorizationState.NETWORK_ERROR_LOGIN
                is LogInResponse.Success       -> AuthorizationState.SUCCESS
            }
        }
    }

    fun signUp(username: String, password: String) {
        authorize(username, password) {
            TODO()
        }
    }


    private inline fun authorize(
        username: String,
        password: String,
        crossinline makeNetworkCall: () -> AuthorizationState ) {

        if(mldState.value != AuthorizationState.IDLE) {
            return
        }

        // TODO: Check internet.

        if(!validate(username, password)) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val state = makeNetworkCall()
            mldState.postValue(state)
        }
    }


    private fun validate(username: String, password: String): Boolean {
        val creds = PrisonerCredentials(username, password)

        if(!creds.isUsernameValid) {
            mldState.value = AuthorizationState.USERNAME_INVALID
            return false
        }

        if(!creds.isPasswordValid) {
            mldState.value = AuthorizationState.PASSWORD_INVALID
            return false
        }

        return true
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): AuthorizationViewModel {

            val fact = AuthorizationVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(AuthorizationViewModel::class.java)
        }
    }
}