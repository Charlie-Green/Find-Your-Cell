package by.sviazen.prisoner.ui.celledit.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CellEditorVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CellEditorEntryPoint {
        val cellEditorViewModel: CellEditorViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = CellEditorEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.cellEditorViewModel as T
    }


    companion object {
        private var instance: CellEditorVMFactory? = null

        fun get(appContext: Context): CellEditorVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CellEditorVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}