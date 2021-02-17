package by.zenkevich_churun.findcell.result.ui.contact.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CoPrisonerContactsVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CoPrisonerContactsEntryPoint {
        val coPrisonerContactsViewModel: CoPrisonerContactsViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = CoPrisonerContactsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.coPrisonerContactsViewModel as T
    }


    companion object {
        private var instance: CoPrisonerContactsVMFactory? = null

        fun get(appContext: Context): CoPrisonerContactsVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CoPrisonerContactsVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}