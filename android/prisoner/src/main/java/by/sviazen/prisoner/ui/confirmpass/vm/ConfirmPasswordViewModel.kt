package by.sviazen.prisoner.ui.confirmpass.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.prisoner.ui.common.auth.ConfirmedPasswordChannel
import by.sviazen.prisoner.ui.common.auth.PrisonerCredentials
import kotlinx.coroutines.launch
import javax.inject.Inject


class ConfirmPasswordViewModel @Inject constructor(
    private val passChannel: ConfirmedPasswordChannel
): ViewModel() {

    /** @return whether the password is valid. **/
    fun submit(password: String): Boolean {
        val creds = PrisonerCredentials("", password)
        if(!creds.isPasswordValid) {
            return false
        }

        viewModelScope.launch {
            passChannel.send(password)
        }
        return true
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ConfirmPasswordViewModel {

            val fact = ConfirmPasswordVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ConfirmPasswordViewModel::class.java)
        }
    }
}