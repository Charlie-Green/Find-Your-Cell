package by.sviazen.prisoner.ui.root.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.sviazen.app.ui.vm.PrisonerRootViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class PrisonerRootVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface PrisonerRootEntryPoint {
        val prisonerRootViewModel: PrisonerRootViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = PrisonerRootEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.prisonerRootViewModel as T
    }


    companion object {
        private var instance: PrisonerRootVMFactory? = null

        fun get(appContext: Context): PrisonerRootVMFactory {
            return instance ?: synchronized(this) {
                instance ?: PrisonerRootVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}