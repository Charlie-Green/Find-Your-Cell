package by.zenkevich_churun.findcell.prisoner.ui.sched.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent


internal class ScheduleVMFactory(
    private val appContext: Context
): ViewModelProvider.Factory {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface ScheduleEntryPoint {
        val scheduleViewModel: ScheduleViewModel
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val entryClass = ScheduleEntryPoint::class.java
        val accessor = EntryPointAccessors.fromApplication(appContext, entryClass)
        return accessor.scheduleViewModel as T
    }


    companion object {
        private var instance: ScheduleVMFactory? = null

        fun get(appContext: Context): ScheduleVMFactory {
            return instance ?: synchronized(this) {
                instance ?: ScheduleVMFactory(appContext).also {
                    instance = it
                }
            }
        }
    }
}