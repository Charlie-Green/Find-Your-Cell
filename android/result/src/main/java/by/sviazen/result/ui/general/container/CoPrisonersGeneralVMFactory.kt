package by.sviazen.result.ui.general.container

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CoPrisonersGeneralVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface OutcomingRequestsEntryPoint {
        val coPrisonersGeneralViewModel: CoPrisonersGeneralViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = OutcomingRequestsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.coPrisonersGeneralViewModel as T
    }


    companion object {
        private var instance: CoPrisonersGeneralVMFactory? = null

        fun get(appContext: Context): CoPrisonersGeneralVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CoPrisonersGeneralVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}