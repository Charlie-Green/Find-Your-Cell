package by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.prisoner.repo.sched.ScheduleRepository
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.model.CellOptionsMode
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleModel
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.ScheduleLivesDataStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellOptionsViewModel @Inject constructor(
    private val scheduleStore: ScheduleLivesDataStorage,
    private val jailRepo: JailsRepository,
    private val scheduleRepo: ScheduleRepository
): ViewModel() {

    private val mldData = MutableLiveData<Cell>()
    private val mldLoading = MutableLiveData<Boolean>()
    private val mldMode = MutableLiveData<CellOptionsMode>().apply {
        value = CellOptionsMode.OPTIONS
    }


    val dataLD: LiveData<Cell>
        get() = mldData

    val modeLD: LiveData<CellOptionsMode>
        get() = mldMode

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = scheduleStore.cellUpdateLD


    fun requestData(jailId: Int, cellNumber: Short) {
        if(mldData.value != null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loadData(jailId, cellNumber)
        }
    }


    fun update() {
        mldData.value?.also { cell ->
            scheduleStore.requestCellUpdate(cell)
        }
    }

    fun delete() {
        mldMode.value = CellOptionsMode.CONFIRM_DELETE
    }

    fun confirmDelete() {
        val cell = mldData.value ?: return

        if(mldMode.value != CellOptionsMode.CONFIRM_DELETE) {
            return
        }
        if(!getAndSetLoading()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            deleteCell(cell)
            mldLoading.postValue(false)
        }

    }

    fun declineDelete() {
        mldMode.value = CellOptionsMode.OPTIONS
    }


    private fun loadData(jailId: Int, cellNumber: Short) {
        // TODO: Provide the real value of 'internet' parameter.
        jailRepo.cell(jailId, cellNumber, true)?.also { cell ->
            mldData.postValue(cell)
        }
    }

    private fun deleteCell(cell: Cell) {
        val deleted = scheduleRepo.deleteCell(cell.jailId, cell.number)
        if(!deleted) {
            scheduleStore.submitCellUpdate(CellUpdate.DeleteFailed)
            return
        }

        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value
            schedule?.deleteCell(cell.jailId, cell.number)
        }

        scheduleStore.submitCellUpdate(CellUpdate.Deleted)
    }


    private fun getAndSetLoading(): Boolean {
        if(mldLoading.value == true) {
            return false
        }

        mldLoading.value = true
        return true
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