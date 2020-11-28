package by.zenkevich_churun.findcell.prisoner.ui.common.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrisonerLiveDatasStorage @Inject constructor() {

    private val mldUpdateScheduleResult = MutableLiveData<UpdateScheduleResult.Success?>()

    val updateScheduleResultLD: LiveData<UpdateScheduleResult.Success?>
        get() = mldUpdateScheduleResult


    fun submitUpdateScheduleSuccess() {
        mldUpdateScheduleResult.postValue(UpdateScheduleResult.Success)
    }

    fun notifyUpdateScheduleResultConsumed() {
        mldUpdateScheduleResult.postValue(null)
    }
}