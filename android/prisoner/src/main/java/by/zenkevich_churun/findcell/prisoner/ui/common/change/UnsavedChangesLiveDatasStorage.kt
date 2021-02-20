package by.zenkevich_churun.findcell.prisoner.ui.common.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UnsavedChangesLiveDatasStorage @Inject constructor() {

    private val mldSchedule = MutableLiveData<Boolean>().apply {
        value = false
    }


    val scheduleLD: LiveData<Boolean>
        get() = mldSchedule


    fun setSchedule(hasUnsavedChanges: Boolean) {
        mldSchedule.postValue(hasUnsavedChanges)
    }
}