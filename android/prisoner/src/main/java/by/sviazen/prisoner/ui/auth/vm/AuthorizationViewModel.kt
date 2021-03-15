package by.sviazen.prisoner.ui.auth.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.repo.profile.ProfileRepository
import by.sviazen.core.repo.profile.SignUpResult
import by.sviazen.domain.contract.auth.LogInResponse
import by.sviazen.prisoner.ui.auth.model.AuthorizationState
import by.sviazen.prisoner.ui.common.auth.ConfirmedPasswordChannel
import by.sviazen.prisoner.ui.common.auth.PrisonerCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AuthorizationViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val netTracker: NetworkStateTracker,
    private val passwordChannel: ConfirmedPasswordChannel
): ViewModel() {

    private val mldState = MutableLiveData<AuthorizationState>().apply {
        value = AuthorizationState.Idle
    }

    val stateLD: LiveData<AuthorizationState>
        get() = mldState


    fun logIn(username: String, password: String) {
        authorize(username, password) {
            when(val response = repo.logIn(username, password)) {
                is LogInResponse.WrongUsername -> AuthorizationState.WrongUsername
                is LogInResponse.WrongPassword -> AuthorizationState.WrongPassword
                is LogInResponse.NetworkError  -> AuthorizationState.NetworkError(true)
                is LogInResponse.Success       -> AuthorizationState.Success
            }
        }
    }

    /** Check the pre-conditions and, if they apply, await for another [ViewModel]
      * to signal the confirmed password and then perform sign up.
      * @return - true, if the pre-conditions check succeeded
      *               and the UI should suggest the user to confirm the password.
      *         - false, if it didn't. The corresponding [AuthorizationState]
      *           will be emitted via [stateLD] in this case.      **/
    fun awaitPasswordConfirm(
        username: String,
        password: String
    ): Boolean {

        if(!checkInternet() || !validate(username, password)) {
            return false
        }

        viewModelScope.launch {
            val confirmedPassword = passwordChannel.receive()
            signUp(username, password, confirmedPassword)
        }

        return true
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

    private suspend fun signUp(
        username: String,
        originalPassword: String,
        confirmedPassword: String ) {

        if(mldState.value !is AuthorizationState.Idle) {
            return
        }
        mldState.value = AuthorizationState.Loading

        withContext(Dispatchers.IO) {
            val result = repo.signUp(username, originalPassword, confirmedPassword)
            applyResult(username, result)
        }
    }

    private fun applyResult(username: String, result: SignUpResult) {
        val newState = when(result) {
            is SignUpResult.WrongConfirmedPassword -> AuthorizationState.PasswordNotConfirmed
            is SignUpResult.NetworkError           -> AuthorizationState.NetworkError(false)
            is SignUpResult.UsernameTaken          -> AuthorizationState.UsernameTaken(username)
            is SignUpResult.Success                -> AuthorizationState.Success
        }

        mldState.postValue(newState)
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