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


    val scheduleLD: LiveData<ScheduleModel>
        get() = mldSchedule

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = mldCellUpdate

    val cellOptionsLD: LiveData<Cell?>
        get() = mldCellOptions


    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
    }


    fun notifyCellAdded() {
        mldCellUpdate.postValue(CellUpdate.Added)
    }

    fun notifyCellUpdated() {
        mldCellUpdate.postValue(CellUpdate.Updated)
    }

    fun notifyCellUpdateConsumed() {
        mldCellUpdate.postValue(null)
    }


    fun requestOptions(cell: Cell) {
        mldCellOptions.value = cell
    }

    fun notifyCellOptionsSuggested() {
        mldCellOptions.value = null
    }
}