package by.sviazen.prisoner.ui.cellopt.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CellOptionsVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CellOptionsEntryPoint {
        val cellOptionsViewModel: CellOptionsViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = CellOptionsEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.cellOptionsViewModel as T
    }


    companion object {
        private var instance: CellOptionsVMFactory? = null

        fun get(appContext: Context): CellOptionsVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CellOptionsVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}