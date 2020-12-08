package by.zenkevich_churun.findcell.prisoner.ui.root.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.PrisonerLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.ScheduleLivesDataStorage
import javax.inject.Inject


class PrisonerRootViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val prisonerStore: PrisonerLiveDatasStorage,
    private val scheduleStore: ScheduleLivesDataStorage
): ViewModel() {

    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD

    val updateScheduleResultLD: LiveData<UpdateScheduleResult.Success?>
        get() = prisonerStore.updateScheduleResultLD

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = scheduleStore.cellUpdateLD


    fun notifySaveResultConsumed()
        = repo.notifySaveResultConsumed()

    fun notifyUpdateScheduleResultConsumed()
        = prisonerStore.notifyUpdateScheduleResultConsumed()

    fun notifyCellUpdateConsumed()
        = scheduleStore.notifyCellUpdateConsumed()


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