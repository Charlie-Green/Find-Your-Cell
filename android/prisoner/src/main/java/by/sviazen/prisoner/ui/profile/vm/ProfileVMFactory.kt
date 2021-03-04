package by.sviazen.prisoner.ui.profile.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class ProfileVMFactory private constructor(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface ProfileVMEntryPoint {
        val profileViewModel: ProfileViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = ProfileVMEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.profileViewModel as T
    }


    companion object {
        private var instance: ProfileVMFactory? = null

        fun get(appContext: Context): ProfileVMFactory {
            return instance ?: synchronized(this) {
                instance ?: ProfileVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}