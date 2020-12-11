package by.zenkevich_churun.findcell.prisoner.ui.arest.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class ArestsVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface ArestsEntryPoint {
        val arestsViewModel: ArestsViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = ArestsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.arestsViewModel as T
    }


    companion object {
        private var instance: ArestsVMFactory? = null

        fun get(appContext: Context): ArestsVMFactory {
            return instance ?: synchronized(this) {
                instance ?: ArestsVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}