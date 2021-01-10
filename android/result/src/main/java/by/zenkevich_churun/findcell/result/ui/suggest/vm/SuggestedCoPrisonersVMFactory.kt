package by.zenkevich_churun.findcell.result.ui.suggest.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class SuggestedCoPrisonersVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface SuggestedCoPrisonersEntryPoint {
        val suggestedCoPrisonersViewModel: SuggestedCoPrisonersViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = SuggestedCoPrisonersEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.suggestedCoPrisonersViewModel as T
    }


    companion object {
        private var instance: SuggestedCoPrisonersVMFactory? = null

        fun get(appContext: Context): SuggestedCoPrisonersVMFactory {
            return instance ?: synchronized(this) {
                instance ?: SuggestedCoPrisonersVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}