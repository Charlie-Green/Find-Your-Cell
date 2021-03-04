package by.sviazen.prisoner.ui.common.sched.ld

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.prisoner.ui.common.sched.period.ScheduleCellsCrudState
import by.sviazen.prisoner.ui.common.sched.period.ScheduleModel
import by.sviazen.prisoner.ui.sched.model.ScheduleCrudState
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleLiveDatasStorage @Inject constructor() {

    private val mldSchedule = MutableLiveData<ScheduleModel?>()

    private val mldScheduleCrudState = MutableLiveData<ScheduleCrudState>().apply {
        value = ScheduleCrudState.Idle
    }

    private val mldCellsCrudState = MutableLiveData<ScheduleCellsCrudState>().apply {
        value = ScheduleCellsCrudState.Idle
    }


    val scheduleLD: LiveData<ScheduleModel?>
        get() = mldSchedule

    val cellsCrudStateLD: LiveData<ScheduleCellsCrudState>
        get() = mldCellsCrudState

    val scheduleCrudStateLD: LiveData<ScheduleCrudState>
        get() = mldScheduleCrudState


    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
    }

    fun clearSchedule() {
        mldSchedule.postValue(null)
    }


    fun submitScheduleCrud(state: ScheduleCrudState) {
        AndroidUtil.setOrPost(mldScheduleCrudState, state)
    }

    fun submitCellsCrud(state: ScheduleCellsCrudState) {
        AndroidUtil.setOrPost(mldCellsCrudState, state)
    }
}