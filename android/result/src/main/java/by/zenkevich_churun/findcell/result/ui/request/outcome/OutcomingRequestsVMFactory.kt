package by.zenkevich_churun.findcell.result.ui.request.outcome

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class OutcomingRequestsVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface OutcomingRequestsEntryPoint {
        val outcomingRequestsViewModel: OutcomingRequestsViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = OutcomingRequestsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.outcomingRequestsViewModel as T
    }


    companion object {
        private var instance: OutcomingRequestsVMFactory? = null

        fun get(appContext: Context): OutcomingRequestsVMFactory {
            return instance ?: synchronized(this) {
                instance ?: OutcomingRequestsVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}