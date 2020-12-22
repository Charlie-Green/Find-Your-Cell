package by.zenkevich_churun.findcell.prisoner.ui.root.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.prisoner.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.prisoner.repo.profile.SavePrisonerResult
import by.zenkevich_churun.findcell.prisoner.repo.sched.UpdateScheduleResult
import by.zenkevich_churun.findcell.prisoner.ui.common.change.UnsavedChangesLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.ui.common.interrupt.InterruptLiveDataStorage
import by.zenkevich_churun.findcell.prisoner.ui.common.interrupt.EditInterruptState
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleLiveDatasStorage
import javax.inject.Inject


class PrisonerRootViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val scheduleStore: ScheduleLiveDatasStorage,
    private val interruptStore: InterruptLiveDataStorage,
    unsavedChangesStore: UnsavedChangesLiveDatasStorage
): ViewModel() {

    val prisonerLD: LiveData<out Prisoner>
        get() = repo.prisonerLD

    val savePrisonerResultLD: LiveData<SavePrisonerResult>
        get() = repo.savePrisonerResultLD

    val updateScheduleResultLD: LiveData<UpdateScheduleResult.Success?>
        get() = scheduleStore.updateScheduleResultLD

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = scheduleStore.cellUpdateLD

    val editInterruptStateLD: LiveData<EditInterruptState>
        get() = interruptStore.stateLD

    val unsavedChangesLD: LiveData<Boolean>
        = UnsavedPrisonerChangesLiveData(unsavedChangesStore, repo)


    fun notifySaveResultConsumed()
        = repo.notifySaveResultConsumed()

    fun notifyUpdateScheduleResultConsumed()
        = scheduleStore.notifyUpdateScheduleResultConsumed()

    fun notifyCellUpdateConsumed()
        = scheduleStore.notifyCellUpdateConsumed()

    fun notifyEditInterrupted(currentDest: Int, desiredDest: Int)
        = interruptStore.interrupt(currentDest, desiredDest)

    fun notifyInterruptConfirmationConsumed()
        = interruptStore.notifyConfirmationConsumed()


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