package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleLiveDatasStorage @Inject constructor() {

    private val mldSchedule = MutableLiveData<ScheduleModel?>()

    private val mldUpdateScheduleResult = MutableLiveData<UpdateScheduleResult.Success?>()

    private val mldCellsCrudState = MutableLiveData<ScheduleCellsCrudState>().apply {
        value = ScheduleCellsCrudState.Idle
    }


    val scheduleLD: LiveData<ScheduleModel?>
        get() = mldSchedule

    val cellsCrudStateLD: LiveData<ScheduleCellsCrudState>
        get() = mldCellsCrudState

    val updateScheduleResultLD: LiveData<UpdateScheduleResult.Success?>
        get() = mldUpdateScheduleResult


    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
    }

    fun clearSchedule() {
        mldSchedule.postValue(null)
    }


    fun submitCellsCrud(state: ScheduleCellsCrudState) {
        mldCellsCrudState.postValue(state)
    }


    fun submitUpdateScheduleSuccess() {
        mldUpdateScheduleResult.postValue(UpdateScheduleResult.Success)
    }

    fun notifyUpdateScheduleResultConsumed() {
        mldUpdateScheduleResult.postValue(null)
    }
}