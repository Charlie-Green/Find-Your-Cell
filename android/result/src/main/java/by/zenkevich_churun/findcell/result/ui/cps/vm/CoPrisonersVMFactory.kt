package by.zenkevich_churun.findcell.result.ui.cps.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class CoPrisonersVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CoPrisonersEntryPoint {
        val coPrisonersViewModel: CoPrisonersViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = CoPrisonersEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.coPrisonersViewModel as T
    }


    companion object {
        private var instance: CoPrisonersVMFactory? = null

        fun get(appContext: Context): CoPrisonersVMFactory {
            return instance ?: synchronized(this) {
                instance ?: CoPrisonersVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}