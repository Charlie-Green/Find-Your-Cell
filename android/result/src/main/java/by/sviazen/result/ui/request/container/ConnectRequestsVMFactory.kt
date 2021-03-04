package by.sviazen.result.ui.request.container

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class ConnectRequestsVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface ConnectRequestsEntryPoint {
        val connectRequestsViewModel: ConnectRequestsViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = ConnectRequestsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.connectRequestsViewModel as T
    }


    companion object {
        private var instance: ConnectRequestsVMFactory? = null

        fun get(appContext: Context): ConnectRequestsVMFactory {
            return instance ?: synchronized(this) {
                instance ?: ConnectRequestsVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}