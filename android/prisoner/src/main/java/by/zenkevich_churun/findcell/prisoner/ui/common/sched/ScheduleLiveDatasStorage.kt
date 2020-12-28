package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleLiveDatasStorage @Inject constructor() {
    private val mldSchedule = MutableLiveData<ScheduleModel?>()
    private val mldCellUpdate = MutableLiveData<CellUpdate?>()
    private val mldCellOptions = MutableLiveData<Cell?>()
    private val mldCellUpdateRequest = MutableLiveData<Cell?>()
    private val mldUpdateScheduleResult = MutableLiveData<UpdateScheduleResult.Success?>()


    val scheduleLD: LiveData<ScheduleModel?>
        get() = mldSchedule

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = mldCellUpdate

    val cellOptionsLD: LiveData<Cell?>
        get() = mldCellOptions

    val cellUpdateRequestLD: LiveData<Cell?>
        get() = mldCellUpdateRequest

    val updateScheduleResultLD: LiveData<UpdateScheduleResult.Success?>
        get() = mldUpdateScheduleResult


    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
    }

    fun clearSchedule() {
        mldSchedule.postValue(null)
    }


    fun requestCellUpdate(cell: Cell) {
        mldCellUpdateRequest.value = cell
    }

    fun submitCellUpdate(update: CellUpdate) {
        mldCellUpdate.postValue(update)
    }

    fun notifyCellUpdateConsumed() {
        mldCellUpdate.postValue(null)
    }

    fun notifyCellUpdateRequestConsumed() {
        mldCellUpdateRequest.postValue(null)
    }


    fun requestOptions(cell: Cell) {
        mldCellOptions.value = cell
    }

    fun notifyCellOptionsSuggested() {
        mldCellOptions.value = null
    }


    fun submitUpdateScheduleSuccess() {
        mldUpdateScheduleResult.postValue(UpdateScheduleResult.Success)
    }

    fun notifyUpdateScheduleResultConsumed() {
        mldUpdateScheduleResult.postValue(null)
    }
}