package by.zenkevich_churun.findcell.prisoner.ui.celledit.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.general.Jail
import by.zenkevich_churun.findcell.core.util.std.max
import by.zenkevich_churun.findcell.prisoner.ui.common.vm.ScheduleLivesDataStorage
import by.zenkevich_churun.findcell.prisoner.repo.jail.GetJailsResult
import by.zenkevich_churun.findcell.prisoner.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.prisoner.repo.sched.ScheduleRepository
import by.zenkevich_churun.findcell.prisoner.ui.celledit.model.*
import by.zenkevich_churun.findcell.prisoner.ui.common.model.CellUpdate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CellEditorViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val repo: JailsRepository,
    private val scheduleRepo: ScheduleRepository,
    private val scheduleStore: ScheduleLivesDataStorage
): ViewModel() {

    private val mapping = CellEditorVMMapping(appContext)
    private val mldEditorState = MutableLiveData<CellEditorState?>()
    private val mldLoading = MutableLiveData<Boolean>()
    private val mldError = MutableLiveData<String?>()


    val editorStateLD: LiveData<CellEditorState?>
        get() = mldEditorState

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val errorLD: LiveData<String?>
        get() = mldError

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = scheduleStore.cellUpdateLD


    fun requestState(jailId: Int, cellNumber: Short) {
        if(mldEditorState.value != null) {
            return
        }
        if(getAndSetLoading()) {
            return
        }

        mldError.value = null

        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Provide the real value of 'internet' parameter.
            val result = repo.jailsList(true)

            when(result) {
                is GetJailsResult.Success -> {
                    val state = createState(result.jails, jailId, cellNumber)
                    mldEditorState.postValue(state)
                }

                is GetJailsResult.FirstTimeNeedInternet -> {
                    mldError.postValue(mapping.needInternetMessage)
                }

                is GetJailsResult.FirstTimeError -> {
                    mldError.postValue(mapping.getJailsErrorMessage)
                }
            }

            mldLoading.postValue(false)
        }
    }


    fun submitState(state: CellEditorState) {
        synchronized(this) {
            mldEditorState.value = state
        }
    }


    fun save() {
        if(getAndSetLoading()) {
            return
        }

        val state = mldEditorState.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            if(state.isNew) {
                if(scheduleRepo.addCell(state.selectedJail.id, state.cellNumber)) {
                    addToSchedule(state)
                    scheduleStore.submitCellUpdate(CellUpdate.Added)
                } else {
                    mldError.postValue(mapping.addCellFailedMessage)
                }

            } else {

                val isUpdated = scheduleRepo.updateCell(
                    state.oldSelectedJail.id, state.oldCellNumber,
                    state.selectedJail.id, state.cellNumber
                )

                if(isUpdated) {
                    updateInSchedule(state)
                    scheduleStore.submitCellUpdate(CellUpdate.Updated)
                } else {
                    mldError.postValue(mapping.updateCellFailedMessage)
                }
            }

            mldLoading.postValue(false)
        }
    }


    fun notifyUiDismissed() {
        scheduleStore.notifyCellUpdateRequestConsumed()
    }


    private fun getAndSetLoading(): Boolean {
        val isLoading = mldLoading.value ?: false
        mldLoading.value = true
        return isLoading
    }

    private fun createState(
        jails: List<Jail>,
        jailId: Int,
        cellNumber: Short
    ): CellEditorState {

        val jailHeaders = jails.map { jail ->
            JailHeader.from(jail)
        }

        val jailIndex = jails.indexOfFirst { jail ->
            jail.id == jailId
        }

        return CellEditorState(
            jailHeaders,
            if(jailIndex in jails.indices) jailIndex else 0,
            max(cellNumber, 1.toShort())
        )
    }

    private fun addToSchedule(editorState: CellEditorState) {
        val addedCell = cell(editorState) ?: return

        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            schedule.addCell(
                editorState.selectedJail,
                editorState.cellNumber,
                addedCell.seats
            )
        }

        scheduleStore.submitCellUpdate(CellUpdate.Added)
    }

    private fun updateInSchedule(editorState: CellEditorState) {
        val updatedCell = cell(editorState) ?: return

        synchronized(this) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            schedule.updateCell(
                editorState.oldSelectedJail.id, editorState.oldCellNumber,
                editorState.selectedJail, editorState.cellNumber,
                updatedCell.seats
            )
        }

        scheduleStore.submitCellUpdate(CellUpdate.Updated)
    }


    private fun cell(editorState: CellEditorState): Cell? {
        // TODO: Get the real value of 'internet' parameter.
        return repo.cell(
            editorState.selectedJail.id,
            editorState.cellNumber,
            true
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