package by.zenkevich_churun.findcell.prisoner.ui.sched.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Schedule
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.repo.sched.*
import by.zenkevich_churun.findcell.prisoner.ui.common.change.UnsavedChangesLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ScheduleViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val repo: ScheduleRepository,
    private val scheduleStore: ScheduleLiveDatasStorage,
    private val changesStore: UnsavedChangesLiveDatasStorage,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    private val mapping = ScheduleVMMapping(appContext)
    private val mldSelectedCellIndex = MutableLiveData<Int>()
    private val mldError = MutableLiveData<String?>()
    private val mldLoading = MutableLiveData<Boolean>()
    private var requestedArestId = Arest.INVALID_ID


    val selectedCellIndexLD: LiveData<Int>
        get() = mldSelectedCellIndex

    val scheduleLD: LiveData<ScheduleModel?>
        get() = scheduleStore.scheduleLD

    val errorLD: LiveData<String?>
        get() = mldError

    val unsavedChangesLD: LiveData<Boolean>
        get() = changesStore.scheduleLD

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val cellUpdateLD: LiveData<ScheduleCellsCrudState?>
        get() = scheduleStore.cellUpdateLD

    val cellOptionsLD: LiveData<Cell?>
        get() = scheduleStore.cellOptionsLD

    val cellUpdateRequestLD: LiveData<Cell?>
        get() = scheduleStore.cellUpdateRequestLD


    fun loadSchedule(arestId: Int) {
        if(!needFetchSchedule(arestId)) {
            return
        }

        netTracker.doOnAvailable {
            getSchedule(arestId)
        }
    }


    fun selectCell(cellIndex: Int) {
        mldSelectedCellIndex.value = cellIndex
    }

    fun unselectCell() {
        mldSelectedCellIndex.value = -1
    }

    fun notifyErrorConsumed() {
        mldError.value = null
    }

    fun notifyScheduleChanged() {
        changesStore.setSchedule(true)
    }


    fun saveSchedule() {
        val scheduleModel = scheduleLD.value ?: return
        if(!startLoad()) {
            return
        }

        mldSelectedCellIndex.value = -1

        viewModelScope.launch(Dispatchers.IO) {
            val schedule = scheduleModel.toSchedule()
            updateSchedule(schedule)
            mldLoading.postValue(false)
        }
    }

    fun notifyCellUpdateConsumed() {
        scheduleStore.notifyCellUpdateConsumed()
    }


    fun requestOptions(cellIndex: Int) {
        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            if(cellIndex !in schedule.cells.indices) {
                return
            }

            scheduleStore.requestOptions( schedule.cells[cellIndex] )
        }
    }

    fun notifyCellOptionsSuggested() {
        scheduleStore.notifyCellOptionsSuggested()
    }

    fun notifyCellUpdateSuggested() {
        scheduleStore.notifyCellUpdateRequestConsumed()
    }


    private fun needFetchSchedule(arestId: Int): Boolean {
        if(arestId == requestedArestId) {
            return false
        }

        // Ensure that old data is not displayed while loading the new data:
        scheduleStore.clearSchedule()

        requestedArestId = arestId
        return true
    }

    private fun getSchedule(arestId: Int) {

        mldLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repo.getSchedule(arestId)) {
                is GetScheduleResult.Success -> {
                    val scheduleModel = ScheduleModel.from(result.schedule)
                    scheduleStore.submitSchedule(scheduleModel)
                }

                is GetScheduleResult.Failed -> {
                    mldError.postValue(mapping.getFailedMessage)
                }
            }

            mldLoading.postValue(false)
        }
    }

    private fun updateSchedule(schedule: Schedule) {
        val result = repo.updateSchedule(schedule)

        if(result is UpdateScheduleResult.Success) {
            scheduleStore.submitUpdateScheduleSuccess()
            changesStore.setSchedule(false)
        } else {
            mldError.postValue(mapping.updateFailedMessage)
        }
    }


    private fun startLoad(): Boolean {
        if(mldLoading.value == true) {
            return false
        }

        mldLoading.postValue(true)
        return true
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ScheduleViewModel {

            val fact = ScheduleVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ScheduleViewModel::class.java)
        }
    }
}