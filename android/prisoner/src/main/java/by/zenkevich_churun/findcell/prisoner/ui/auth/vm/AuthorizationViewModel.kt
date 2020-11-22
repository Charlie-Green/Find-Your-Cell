package by.zenkevich_churun.findcell.prisoner.ui.auth.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import javax.inject.Inject


class AuthorizationViewModel @Inject constructor(): ViewModel() {

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