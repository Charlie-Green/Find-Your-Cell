package by.sviazen.prisoner.ui.sched.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Schedule
import by.sviazen.prisoner.ui.common.sched.ld.ScheduleLiveDatasStorage
import by.sviazen.prisoner.repo.sched.*
import by.sviazen.prisoner.repo.sched.result.GetScheduleResult
import by.sviazen.prisoner.repo.sched.result.UpdateScheduleResult
import by.sviazen.prisoner.ui.common.change.UnsavedChangesLiveDatasStorage
import by.sviazen.prisoner.ui.common.sched.period.ScheduleCellsCrudState
import by.sviazen.prisoner.ui.common.sched.period.ScheduleModel
import by.sviazen.prisoner.ui.sched.model.ScheduleCrudState
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

    private val mldSelectedCellIndex = MutableLiveData<Int>()
    private var requestedArestId = Arest.INVALID_ID


    val selectedCellIndexLD: LiveData<Int>
        get() = mldSelectedCellIndex

    val scheduleStateLD: LiveData<ScheduleCrudState>
        get() = scheduleStore.scheduleCrudStateLD

    val scheduleLD: LiveData<ScheduleModel?>
        get() = scheduleStore.scheduleLD

    val unsavedChangesLD: LiveData<Boolean>
        get() = changesStore.scheduleLD

    val cellCrudStateLD: LiveData<ScheduleCellsCrudState>
        get() = scheduleStore.cellsCrudStateLD


    fun loadSchedule(arestId: Int) {
        if(!needFetchSchedule(arestId)) {
            return
        }

        netTracker.doOnAvailable {
            getSchedule(arestId)
        }
    }


    fun swapCellSelection(cellIndex: Int) {
        mldSelectedCellIndex.value =
            if(mldSelectedCellIndex.value == cellIndex) -1
            else cellIndex
    }

    fun unselectCell() {
        mldSelectedCellIndex.value = -1
    }

    fun notifyScheduleChanged() {
        changesStore.setSchedule(true)
    }

    fun addCell() {
        scheduleStore.submitCellsCrud(ScheduleCellsCrudState.AddRequested)
    }


    fun saveSchedule() {
        val scheduleModel = scheduleLD.value ?: return
        mldSelectedCellIndex.value = -1
        scheduleStore.submitScheduleCrud(ScheduleCrudState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            val schedule = scheduleModel.toSchedule()
            updateSchedule(schedule)
        }
    }

    fun requestOptions(cellIndex: Int) {
        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            if(cellIndex !in schedule.cells.indices) {
                return
            }

            mldSelectedCellIndex.value = -1

            val cell = schedule.cells[cellIndex]
            val newState = ScheduleCellsCrudState.ViewingOptions(cell)
            scheduleStore.submitCellsCrud(newState)
        }
    }


    private fun needFetchSchedule(arestId: Int): Boolean {
        if(arestId == requestedArestId) {
            return false
        }

        // Ensure that old data is not displayed while loading the new data:
        synchronized(scheduleStore) {
            scheduleStore.clearSchedule()
            scheduleStore.submitScheduleCrud(ScheduleCrudState.Idle)
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.Idle)
        }

        requestedArestId = arestId
        return true
    }

    private fun getSchedule(arestId: Int) {
        scheduleStore.submitScheduleCrud(ScheduleCrudState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repo.getSchedule(arestId)) {
                is GetScheduleResult.Success -> {
                    val scheduleModel = ScheduleModel.from(result.schedule)
                    scheduleStore.submitSchedule(scheduleModel)
                    scheduleStore.submitScheduleCrud(ScheduleCrudState.Got)
                }

                is GetScheduleResult.Failed -> {
                    scheduleStore.submitScheduleCrud(ScheduleCrudState.GetFailed())
                }
            }
        }
    }

    private fun updateSchedule(schedule: Schedule) {
        val result = repo.updateSchedule(schedule)

        if(result is UpdateScheduleResult.Success) {
            scheduleStore.submitScheduleCrud(ScheduleCrudState.Updated())
            changesStore.setSchedule(false)
        } else {
            scheduleStore.submitScheduleCrud(ScheduleCrudState.UpdateFailed())
        }
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