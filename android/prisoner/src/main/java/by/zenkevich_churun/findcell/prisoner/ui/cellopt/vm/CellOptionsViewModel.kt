package by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.ScheduleLivesDataStorage
import javax.inject.Inject


class CellOptionsViewModel @Inject constructor(
    private val scheduleStore: ScheduleLivesDataStorage
): ViewModel() {

    fun notifyUiDismissed() {
        scheduleStore.notifyCellOptionsSuggested()
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CellOptionsViewModel {

            val fact = CellOptionsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CellOptionsViewModel::class.java)
        }
    }
}