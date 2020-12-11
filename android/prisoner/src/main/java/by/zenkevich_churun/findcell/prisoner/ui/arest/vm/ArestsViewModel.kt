package by.zenkevich_churun.findcell.prisoner.ui.arest.vm

import android.content.Context
import androidx.lifecycle.*
import javax.inject.Inject


class ArestsViewModel @Inject constructor(): ViewModel() {

    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ArestsViewModel {

            val fact = ArestsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ArestsViewModel::class.java)
        }
    }
}