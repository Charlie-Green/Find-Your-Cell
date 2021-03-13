package by.sviazen.prisoner.ui.auth.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.repo.profile.ProfileRepository
import by.sviazen.domain.contract.auth.LogInResponse
import by.sviazen.domain.contract.auth.SignUpResponse
import by.sviazen.prisoner.ui.auth.model.AuthorizationState
import by.sviazen.prisoner.ui.auth.model.PrisonerCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthorizationViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    private val mldState = MutableLiveData<AuthorizationState>().apply {
        value = AuthorizationState.Idle
    }

    val stateLD: LiveData<AuthorizationState>
        get() = mldState


    fun logIn(username: String, password: String) {
        authorize(username, password) {
            when(val response = repo.logIn(username, password)) {
                is LogInResponse.WrongUsername -> AuthorizationState.UsernameNotExist
                is LogInResponse.WrongPassword -> AuthorizationState.PasswordNotMatch
                is LogInResponse.NetworkError  -> AuthorizationState.NetworkError(true)
                is LogInResponse.Success       -> AuthorizationState.Success
            }
        }
    }

    fun signUp(username: String, password: String) {
        authorize(username, password) {
            when(val response = repo.signUp(username, password)) {
                is SignUpResponse.NetworkError  -> AuthorizationState.NetworkError(false)
                is SignUpResponse.UsernameTaken -> AuthorizationState.UsernameTaken(username)
                is SignUpResponse.Success       -> AuthorizationState.Success
            }
        }
    }


    fun notifyStateConsumed() {
        mldState.value = AuthorizationState.Idle
    }


    private inline fun authorize(
        username: String,
        password: String,
        crossinline makeNetworkCall: () -> AuthorizationState ) {

        if(mldState.value != AuthorizationState.Idle ||
            !validate(username, password) ||
            !checkInternet() ) {

            return
        }

        mldState.value = AuthorizationState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val state = makeNetworkCall()
            mldState.postValue(state)
        }
    }


    private fun validate(username: String, password: String): Boolean {
        val creds = PrisonerCredentials(username, password)

        if(!creds.isUsernameValid) {
            mldState.value = AuthorizationState.InvalidUsername
            return false
        }

        if(!creds.isPasswordValid) {
            mldState.value = AuthorizationState.InvalidPassword
            return false
        }

        return true
    }

    private fun checkInternet(): Boolean {
        if(netTracker.isInternetAvailable) {
            return true
        }

        mldState.value = AuthorizationState.NoInternet
        return false
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