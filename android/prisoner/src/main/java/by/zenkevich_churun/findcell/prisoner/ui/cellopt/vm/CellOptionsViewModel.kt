package by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.ScheduleLivesDataStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellOptionsViewModel @Inject constructor(
    private val scheduleStore: ScheduleLivesDataStorage,
    private val jailRepo: JailsRepository
): ViewModel() {

    private val mldData = MutableLiveData<String>()
    private var cell: Cell? = null


    val dataLD: LiveData<String>
        get() = mldData


    fun requestData(jailId: Int, cellNumber: Short) {
        if(mldData.value != null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loadData(jailId, cellNumber)
        }
    }


    fun update() {
        cell?.also { scheduleStore.requestCellUpdate(it) }
    }


    private fun loadData(jailId: Int, cellNumber: Short) {
        // TODO: Provide the real value of 'internet' parameter.
        jailRepo.cell(jailId, cellNumber, true)?.also { cell ->
            this.cell = cell
            val cellData = "${cell.jailName}, ${cell.number}"
            mldData.postValue(cellData)
        }
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