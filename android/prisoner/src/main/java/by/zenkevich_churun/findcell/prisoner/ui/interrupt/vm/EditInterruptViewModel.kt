package by.zenkevich_churun.findcell.prisoner.ui.interrupt.vm

import android.content.Context
import androidx.lifecycle.*
import javax.inject.Inject


class EditInterruptViewModel @Inject constructor(): ViewModel() {

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