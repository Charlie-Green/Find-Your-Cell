package by.zenkevich_churun.findcell.prisoner.ui.celledit.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.domain.entity.Cell
import by.zenkevich_churun.findcell.domain.entity.Jail
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.repo.jail.GetJailsResult
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.prisoner.repo.sched.ScheduleRepository
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.CellEditFailureReason
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.JailHeader
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellEditorViewModel @Inject constructor(
    private val repo: JailsRepository,
    private val scheduleRepo: ScheduleRepository,
    private val scheduleStore: ScheduleLiveDatasStorage,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    val cellCrudStateLD: LiveData<ScheduleCellsCrudState>
        get() = scheduleStore.cellsCrudStateLD


    fun notifyUiShowing() {
        val oldState = cellCrudStateLD.value
        if(oldState !is ScheduleCellsCrudState.AddRequested &&
            oldState !is ScheduleCellsCrudState.UpdateRequested ) {

            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.jailsList(netTracker.isInternetAvailable)

            when(result) {
                is GetJailsResult.Success -> {
                    val newState = createState(result.jails, oldState)
                    scheduleStore.submitCellsCrud(newState)
                }

                is GetJailsResult.FirstTimeNeedInternet -> {
                    val state = ScheduleCellsCrudState.GetJailsNeedsInternet
                    scheduleStore.submitCellsCrud(state)
                }

                is GetJailsResult.FirstTimeError -> {
                    val state = ScheduleCellsCrudState.GetJailsFailed
                    scheduleStore.submitCellsCrud(state)
                }
            }
        }
    }


    fun submitEditorState(
        jailIndex: Int,
        cellNumber: Short ) {

        val oldState = cellCrudStateLD.value ?: ScheduleCellsCrudState.Idle
        val newState = modifyEditorState(oldState, jailIndex, cellNumber)
        scheduleStore.submitCellsCrud(newState)
    }


    fun save() {
        val state = cellCrudStateLD.value ?: ScheduleCellsCrudState.Idle
        scheduleStore.submitCellsCrud( ScheduleCellsCrudState.Processing )

        viewModelScope.launch(Dispatchers.IO) {
            when(state) {
                is ScheduleCellsCrudState.Editing.Adding -> {
                    saveAdd(state)
                }

                is ScheduleCellsCrudState.Editing.AddFailed -> {
                    saveAdd(state)
                }

                is ScheduleCellsCrudState.Editing.Updating -> {
                    saveUpdate(state, state.original)
                }

                is ScheduleCellsCrudState.Editing.UpdateFailed -> {
                    saveUpdate(state, state.original)
                }

                else -> {
                    scheduleStore.submitCellsCrud(state)
                }
            }
        }
    }


    private fun saveAdd(state: ScheduleCellsCrudState.Editing) {
        val jail = state.selectedJail
        val cell = cell(state)

        if(cellPresent(state.selectedJail, state.cellNumber)) {
            val newState = ScheduleCellsCrudState.Editing.AddFailed(
                state.jails,
                state.jailIndex,
                state.cellNumber,
                CellEditFailureReason.DUPLICATE
            )

            scheduleStore.submitCellsCrud(newState)
            return
        }

        val added = if(jail == null || cell == null) {
            false
        } else {
            scheduleRepo.addCell(
                jail.id,
                state.cellNumber
            )
        }

        val newState = if(added) {
            addToCache(state)
            ScheduleCellsCrudState.Added(cell!!)

        } else {
            ScheduleCellsCrudState.Editing.AddFailed(
                state.jails,
                state.jailIndex,
                state.cellNumber,
                CellEditFailureReason.NETWORK_ERROR
            )
        }

        scheduleStore.submitCellsCrud(newState)
    }

    private fun saveUpdate(
        state: ScheduleCellsCrudState.Editing,
        originalCell: Cell ) {

        if(cellPresent(state.selectedJail, state.cellNumber)) {
            val newState = ScheduleCellsCrudState.Editing.UpdateFailed(
                originalCell,
                state.jails,
                state.jailIndex,
                state.cellNumber,
                CellEditFailureReason.DUPLICATE
            )

            scheduleStore.submitCellsCrud(newState)
            return
        }

        val jail = state.selectedJail
        val updated = if(jail == null) {
            false
        } else {
            scheduleRepo.updateCell(
                originalCell.jailId,
                originalCell.number,
                jail.id,
                state.cellNumber
            )
        }

        val newState = if(updated) {
            updateInCache(state, originalCell)
            ScheduleCellsCrudState.Updated()
        } else {
            ScheduleCellsCrudState.Editing.UpdateFailed(
                originalCell,
                state.jails,
                state.jailIndex,
                state.cellNumber,
                CellEditFailureReason.NETWORK_ERROR
            )
        }

        scheduleStore.submitCellsCrud(newState)
    }

    private fun cellPresent(
        jail: JailHeader?,
        cellNumber: Short
    ): Boolean {

        jail ?: return false

        val sched = scheduleStore.scheduleLD.value ?: return false
        val duplicate = sched.cells.find { c ->
            c.jailId == jail.id && c.number == cellNumber
        }

        return (duplicate != null)
    }


    fun notifyUiDismissed() {
        synchronized(scheduleStore) {
            val oldState = cellCrudStateLD.value
            if(oldState is ScheduleCellsCrudState.ViewingOptions ||
                oldState is ScheduleCellsCrudState.ConfirmingDelete ) {

                scheduleStore.submitCellsCrud(ScheduleCellsCrudState.Idle)
            }
        }
    }


    private fun createState(
        jails: List<Jail>,
        oldState: ScheduleCellsCrudState
    ): ScheduleCellsCrudState.Editing {

        val jailHeaders = jails.map { jail ->
            JailHeader.from(jail)
        }

        return when(oldState) {

            is ScheduleCellsCrudState.AddRequested -> {
                ScheduleCellsCrudState.Editing.Adding(
                    jailHeaders,
                    0,
                    1
                )
            }

            is ScheduleCellsCrudState.UpdateRequested -> {
                val jailIndex = jails.indexOfFirst { j ->
                    j.id == oldState.original.jailId
                }

                ScheduleCellsCrudState.Editing.Updating(
                    oldState.original,
                    jailHeaders,
                    if(jailIndex in jails.indices) jailIndex else 0,
                    oldState.original.number
                )
            }

            else -> {
                throw IllegalStateException(
                    "Can't initialize Cell Editor from state ${oldState.javaClass.canonicalName}" )
            }
        }


    }

    private fun modifyEditorState(
        oldState: ScheduleCellsCrudState,
        jailIndex: Int,
        cellNumber: Short

    ): ScheduleCellsCrudState = when(oldState) {

        is ScheduleCellsCrudState.Editing.Adding -> {
            ScheduleCellsCrudState.Editing.Adding(
                oldState.jails,
                jailIndex,
                cellNumber
            )
        }

        is ScheduleCellsCrudState.Editing.Updating -> {
            ScheduleCellsCrudState.Editing.Updating(
                oldState.original,
                oldState.jails,
                jailIndex,
                cellNumber
            )
        }

        is ScheduleCellsCrudState.Editing.AddFailed -> {
            ScheduleCellsCrudState.Editing.AddFailed(
                oldState.jails,
                jailIndex,
                cellNumber,
                oldState.reason
            )
        }

        is ScheduleCellsCrudState.Editing.UpdateFailed -> {
            ScheduleCellsCrudState.Editing.UpdateFailed(
                oldState.original,
                oldState.jails,
                jailIndex,
                cellNumber,
                oldState.reason
            )
        }

        is ScheduleCellsCrudState.Editing -> {
            // Cases for all Editing states must be handled!
            throw NotImplementedError(
                "Don't know how to inject editor date into this state: ${oldState.javaClass.name}" )
        }

        else -> {
            oldState
        }
    }

    private fun addToCache(state: ScheduleCellsCrudState.Editing) {
        val addedCell = cell(state) ?: return

        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            schedule.addCell(
                state.selectedJail ?: return,
                state.cellNumber,
                addedCell.seats
            )
        }
    }

    private fun updateInCache(
        state: ScheduleCellsCrudState.Editing,
        originalCell: Cell ) {

        val newCell = cell(state) ?: return

        synchronized(this) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            schedule.updateCell(
                originalCell.jailId,
                originalCell.number,
                state.selectedJail ?: return,
                state.cellNumber,
                newCell.seats
            )
        }
    }


    private fun cell(state: ScheduleCellsCrudState.Editing): Cell? {
        return repo.cell(
            state.selectedJail?.id ?: return null,
            state.cellNumber,
            netTracker.isInternetAvailable
        )
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CellEditorViewModel {

            val fact = CellEditorVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CellEditorViewModel::class.java)
        }
    }
}