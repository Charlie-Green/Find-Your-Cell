package by.zenkevich_churun.findcell.prisoner.ui.root.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.PrisonerLiveDatasStorage
import javax.inject.Inject


class PrisonerRootViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val store: PrisonerLiveDatasStorage
): ViewModel() {

    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD

    val updateScheduleResultLD: LiveData<UpdateScheduleResult.Success?>
        get() = store.updateScheduleResultLD


    fun notifySaveResultConsumed() {
        repo.notifySaveResultConsumed()
    }

    fun notifyUpdateScheduleResultConsumed() {
        store.notifyUpdateScheduleResultConsumed()
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