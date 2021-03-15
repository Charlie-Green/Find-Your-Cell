package by.sviazen.prisoner.ui.confirmpass.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class ConfirmPasswordVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface ConfirmPasswordEntryPoint {
        val confirmPasswordViewModel: ConfirmPasswordViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = ConfirmPasswordEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.confirmPasswordViewModel as T
    }


    companion object {
        private var instance: ConfirmPasswordVMFactory? = null

        fun get(appContext: Context): ConfirmPasswordVMFactory {
            return instance ?: synchronized(this) {
                instance ?: ConfirmPasswordVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}