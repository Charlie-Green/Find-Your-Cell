package by.sviazen.prisoner.ui.addarest.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CUArestVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CUArestEntryPoint {
        val cUArestViewModel: CUArestViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = CUArestEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.cUArestViewModel as T
    }


    companion object {
        private var instance: CUArestVMFactory? = null

        fun get(appContext: Context): CUArestVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CUArestVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}