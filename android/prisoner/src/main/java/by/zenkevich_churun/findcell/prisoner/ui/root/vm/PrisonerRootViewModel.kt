package by.zenkevich_churun.findcell.prisoner.ui.root.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import javax.inject.Inject


class PrisonerRootViewModel @Inject constructor(
    private val repo: ProfileRepository
): ViewModel() {

    private val mldUpdateScheduleResult = MutableLiveData<UpdateScheduleResult?>()


    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD

    val updateScheduleResultLD: LiveData<UpdateScheduleResult?>
        get() = mldUpdateScheduleResult

    fun notifySaveResultConsumed() {
        repo.notifySaveResultConsumed()
    }

    fun notifyUpdateScheduleResultConsumed() {
        mldUpdateScheduleResult.postValue(null)
    }

    fun submitUpdateScheduleResult(result: UpdateScheduleResult) {
        mldUpdateScheduleResult.postValue(result)
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): PrisonerRootViewModel {

            val fact = PrisonerRootVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(PrisonerRootViewModel::class.java)
        }
    }
}