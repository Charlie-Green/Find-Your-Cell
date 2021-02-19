package by.zenkevich_churun.findcell.prisoner.ui.common.sched

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.ui.sched.model.ScheduleCrudState
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