package by.zenkevich_churun.findcell.prisoner.ui.cell.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CellVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CellEntryPoint {
        val cellViewModel: CellViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = CellEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.cellViewModel as T
    }


    companion object {
        private var instance: CellVMFactory? = null

        fun get(appContext: Context): CellVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CellVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}