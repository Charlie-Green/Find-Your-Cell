package by.zenkevich_churun.findcell.prisoner.ui.interrupt.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class EditInterruptVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface ResultRequestEntryPoint {
        val editInterruptViewModel: EditInterruptViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = ResultRequestEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.editInterruptViewModel as T
    }


    companion object {
        private var instance: EditInterruptVMFactory? = null

        fun get(appContext: Context): EditInterruptVMFactory {
            return instance ?: synchronized(this) {
                instance ?: EditInterruptVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}