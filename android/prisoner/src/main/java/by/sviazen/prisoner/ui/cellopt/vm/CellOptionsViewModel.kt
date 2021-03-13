package by.sviazen.prisoner.ui.cellopt.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.repo.jail.JailsRepository
import by.sviazen.core.repo.sched.ScheduleRepository
import by.sviazen.domain.entity.Cell
import by.sviazen.prisoner.ui.common.sched.period.ScheduleCellsCrudState
import by.sviazen.prisoner.ui.common.sched.ld.ScheduleLiveDatasStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellOptionsViewModel @Inject constructor(
    private val scheduleStore: ScheduleLiveDatasStorage,
    private val jailRepo: JailsRepository,
    private val scheduleRepo: ScheduleRepository,
    private val netTracker: NetworkStateTracker
): ViewModel() {


    val crudStateLD: LiveData<ScheduleCellsCrudState>
        get() = scheduleStore.cellsCrudStateLD


    fun update() {
        changeState { oldState ->
            ScheduleCellsCrudState.UpdateRequested(oldState.target)
        }
    }

    fun delete() {
        changeState { oldState ->
            ScheduleCellsCrudState.ConfirmingDelete(oldState.target)
        }
    }

    fun confirmDelete() {
        val oldState = crudStateLD.value
        if(oldState !is ScheduleCellsCrudState.ConfirmingDelete) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            deleteCell(oldState.target)
        }

    }

    fun declineDelete() {
        synchronized(scheduleStore) {
            val oldState = crudStateLD.value
            if(oldState !is ScheduleCellsCrudState.ConfirmingDelete) {
                return
            }

            val newState = ScheduleCellsCrudState.ViewingOptions(oldState.target)
            scheduleStore.submitCellsCrud(newState)
        }
    }

    private fun deleteCell(cell: Cell) {
        val deleted = scheduleRepo.deleteCell(cell.jailId, cell.number)
        if(!deleted) {
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.DeleteFailed())
            return
        }

        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value
            schedule?.deleteCell(cell.jailId, cell.number)
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.Deleted())
        }
    }


    private inline fun changeState(
        mapState: (oldState: ScheduleCellsCrudState.ViewingOptions) -> ScheduleCellsCrudState
    ) {

        synchronized(scheduleStore) {
            val oldState = crudStateLD.value
            if(oldState is ScheduleCellsCrudState.ViewingOptions) {
                val newState = mapState(oldState)
                scheduleStore.submitCellsCrud(newState)
            }
        }
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CellOptionsViewModel {

            val fact = CellOptionsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CellOptionsViewModel::class.java)
        }
    }
}