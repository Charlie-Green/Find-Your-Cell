package by.zenkevich_churun.findcell.prisoner.repo.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.prisoner.ui.common.model.ScheduleModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleLiveDataStorage @Inject constructor() {
    private val mldSchedule = MutableLiveData<ScheduleModel>()

    val scheduleLD: LiveData<ScheduleModel>
        get() = mldSchedule

    fun submitSchedule(schedule: ScheduleModel) {
        mldSchedule.postValue(schedule)
    }
}