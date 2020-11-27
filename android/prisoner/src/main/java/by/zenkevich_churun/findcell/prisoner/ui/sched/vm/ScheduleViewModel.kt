package by.zenkevich_churun.findcell.prisoner.ui.sched.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import javax.inject.Inject


class ScheduleViewModel @Inject constructor(
    private val repo: ProfileRepository
): ViewModel() {

    private val mldSelectedCellIndex = MutableLiveData<Int>()


    val selectedCellIndexLD: LiveData<Int>
        get() = mldSelectedCellIndex

    fun selectCell(cellIndex: Int) {
        mldSelectedCellIndex.value = cellIndex
    }

    fun unselectCell() {
        mldSelectedCellIndex.value = -1
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ScheduleViewModel {

            val fact = ScheduleVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ScheduleViewModel::class.java)
        }
    }
}