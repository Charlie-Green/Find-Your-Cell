package by.zenkevich_churun.findcell.prisoner.ui.common.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleLivesDataStorage @Inject constructor() {
    private val mldSchedule = MutableLiveData<ScheduleModel>()
    private val mldCellUpdate = MutableLiveData<CellUpdate?>()
    private val mldCellOptions = MutableLiveData<Cell?>()
    private val mldCellUpdateRequest = MutableLiveData<Cell?>()


    val scheduleLD: LiveData<ScheduleModel>
        get() = mldSchedule

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = mldCellUpdate

    val cellOptionsLD: LiveData<Cell?>
        get() = mldCellOptions

    val cellUpdateRequestLD: LiveData<Cell?>
        get() = mldCellUpdateRequest


    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
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
}