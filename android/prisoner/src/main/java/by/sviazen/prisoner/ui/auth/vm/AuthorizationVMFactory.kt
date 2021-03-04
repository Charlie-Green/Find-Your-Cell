package by.sviazen.prisoner.ui.auth.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class AuthorizationVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface AuthorizationEntryPoint {
        val authorizationViewModel: AuthorizationViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = AuthorizationEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.authorizationViewModel as T
    }


    companion object {
        private var instance: AuthorizationVMFactory? = null

        fun get(appContext: Context): AuthorizationVMFactory {
            return instance ?: synchronized(this) {
                instance ?: AuthorizationVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}