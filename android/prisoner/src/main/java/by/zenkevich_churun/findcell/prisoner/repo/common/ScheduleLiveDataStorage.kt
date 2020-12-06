package by.zenkevich_churun.findcell.prisoner.repo.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleLiveDataStorage @Inject constructor() {
    private val mldSchedule = MutableLiveData<ScheduleModel>()
    private val mldCellUpdate = MutableLiveData<CellUpdate?>()

    val scheduleLD: LiveData<ScheduleModel>
        get() = mldSchedule

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = mldCellUpdate


    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
    }

    fun notifyCellAdded() {
        Log.v("CharlieDebug", "Cell added")
        mldCellUpdate.postValue(CellUpdate.Added)
    }

    fun notifyCellUpdated() {
        Log.v("CharlieDebug", "Cell updated")
        mldCellUpdate.postValue(CellUpdate.Updated)
    }

    fun notifyCellUpdateConsumed() {
        mldCellUpdate.postValue(null)
    }
}