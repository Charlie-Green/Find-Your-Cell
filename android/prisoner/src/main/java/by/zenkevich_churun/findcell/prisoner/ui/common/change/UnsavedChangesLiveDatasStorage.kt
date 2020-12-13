package by.zenkevich_churun.findcell.prisoner.ui.common.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UnsavedChangesLiveDatasStorage @Inject constructor() {
    private val mldSchedule = createMutableLiveData()


    val scheduleLD: LiveData<Boolean>
        get() = mldSchedule


    fun setSchedule(hasUnsavedChanges: Boolean) {
        mldSchedule.value = hasUnsavedChanges
    }


    private fun createMutableLiveData()
        = MutableLiveData<Boolean>().apply { value = false }
}