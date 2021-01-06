package by.zenkevich_churun.findcell.prisoner.ui.celledit.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.Cell
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.repo.jail.GetJailsResult
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.prisoner.repo.sched.ScheduleRepository
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.JailHeader
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleCellsCrudState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellEditorViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val repo: JailsRepository,
    private val scheduleRepo: ScheduleRepository,
    private val scheduleStore: ScheduleLiveDatasStorage,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    val cellCrudStateLD: LiveData<ScheduleCellsCrudState>
        get() = scheduleStore.cellsCrudStateLD


    fun notifyUiShowing() {
        val oldState = cellCrudStateLD.value ?: ScheduleCellsCrudState.Idle
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
        val newState = ScheduleCellsCrudState.Updated()
        scheduleStore.submitCellsCrud(newState)

        val state = cellCrudStateLD.value
        scheduleStore.submitCellsCrud( ScheduleCellsCrudState.Processing )

        viewModelScope.launch(Dispatchers.IO) {
            when(state) {
                is ScheduleCellsCrudState.Editing.Adding -> {
                    saveAdd(state)
                }

                is ScheduleCellsCrudState.Editing.Updating -> {
                    saveUpdate(state)
                }
            }
        }
    }

    private fun saveAdd(state: ScheduleCellsCrudState.Editing.Adding) {
        val jail = state.selectedJail
        val added = if(jail == null) {
            false
        } else {
            scheduleRepo.addCell(
                jail.id,
                state.cellNumber
            )
        }

        if(added) {
            addToCache(state)
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.Added())
        } else {
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.AddFailed())
        }
    }

    private fun saveUpdate(state: ScheduleCellsCrudState.Editing.Updating) {
        val jail = state.selectedJail
        val updated = if(jail == null) {
            false
        } else {
            scheduleRepo.updateCell(
                state.original.jailId,
                state.original.number,
                jail.id,
                state.cellNumber
            )
        }

        if(updated) {
            updateInCache(state)
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.Added())
        } else {
            scheduleStore.submitCellsCrud(ScheduleCellsCrudState.AddFailed())
        }
    }


    fun notifyUiDismissed() {
        scheduleStore.submitCellsCrud(ScheduleCellsCrudState.Idle)
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
                    1
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

        else -> {
            oldState
        }
    }

    private fun addToCache(state: ScheduleCellsCrudState.Editing.Adding) {
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

    private fun updateInCache(state: ScheduleCellsCrudState.Editing.Updating) {
        val cell = cell(state) ?: return

        synchronized(this) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            schedule.updateCell(
                state.original.jailId,
                state.original.number,
                state.selectedJail ?: return,
                state.cellNumber,
                cell.seats
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