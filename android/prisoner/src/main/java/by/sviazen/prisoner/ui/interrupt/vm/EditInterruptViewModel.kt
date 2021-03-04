package by.sviazen.prisoner.ui.interrupt.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.prisoner.repo.profile.ProfileRepository
import by.sviazen.prisoner.ui.common.interrupt.InterruptLiveDataStorage
import javax.inject.Inject


class EditInterruptViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val interruptStore: InterruptLiveDataStorage
): ViewModel() {

    fun notifyInterruptDeclined()
        = interruptStore.decline()

    fun notifyInterruptConfirmed() {
        interruptStore.confirm()
        profileRepo.withdrawUnsavedChanges()
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): EditInterruptViewModel {

            val fact = EditInterruptVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(EditInterruptViewModel::class.java)
        }
    }
}