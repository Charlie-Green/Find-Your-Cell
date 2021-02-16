package by.zenkevich_churun.findcell.result.ui.request.income

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Inject


internal class IncomingRequestsVMFactory @Inject constructor(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface IncomingRequestsEntryPoint {
        val incomingRequestsViewModel: IncomingRequestsViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = IncomingRequestsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.incomingRequestsViewModel as T
    }


    companion object {
        private var instance: IncomingRequestsVMFactory? = null

        fun get(appContext: Context): IncomingRequestsVMFactory {
            return instance ?: synchronized(this) {
                instance ?: IncomingRequestsVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}