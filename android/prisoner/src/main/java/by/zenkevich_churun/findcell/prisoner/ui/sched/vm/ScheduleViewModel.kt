package by.zenkevich_churun.findcell.prisoner.ui.sched.vm

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.core.entity.sched.Schedule
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleLiveDatasStorage
import by.zenkevich_churun.findcell.prisoner.repo.sched.*
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.CellUpdate
import by.zenkevich_churun.findcell.prisoner.ui.common.sched.ScheduleModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ScheduleViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val repo: ScheduleRepository,
    private val scheduleStore: ScheduleLiveDatasStorage
): ViewModel() {

    private val mapping = ScheduleVMMapping(appContext)

    private val mldSelectedCellIndex = MutableLiveData<Int>()
    private val mldError = MutableLiveData<String?>()
    private val mldChanges = MutableLiveData<Boolean>()
    private val mldLoading = MutableLiveData<Boolean>()


    init {
        AndroidUtil.whenInternetAvailable(appContext) { netMan, callback ->
            if(scheduleLD.value == null) {
                getSchedule(netMan, callback)
            }
        }
    }


    val selectedCellIndexLD: LiveData<Int>
        get() = mldSelectedCellIndex

    val scheduleLD: LiveData<ScheduleModel>
        get() = scheduleStore.scheduleLD

    val errorLD: LiveData<String?>
        get() = mldError

    val unsavedChangesLD: LiveData<Boolean>
        get() = mldChanges

    val loadingLD: LiveData<Boolean>
        get() = mldLoading

    val cellUpdateLD: LiveData<CellUpdate?>
        get() = scheduleStore.cellUpdateLD

    val cellOptionsLD: LiveData<Cell?>
        get() = scheduleStore.cellOptionsLD

    val cellUpdateRequestLD: LiveData<Cell?>
        get() = scheduleStore.cellUpdateRequestLD


    fun selectCell(cellIndex: Int) {
        mldSelectedCellIndex.value = cellIndex
    }

    fun unselectCell() {
        mldSelectedCellIndex.value = -1
    }

    fun notifyErrorConsumed() {
        mldError.value = null
    }

    fun notifyScheduleChanged() {
        mldChanges.value = true
    }


    fun saveSchedule() {
        val scheduleModel = scheduleLD.value ?: return
        if(!startLoad()) {
            return
        }

        mldSelectedCellIndex.value = -1

        viewModelScope.launch(Dispatchers.IO) {
            val schedule = scheduleModel.toSchedule()
            updateSchedule(schedule)
            mldLoading.postValue(false)
        }
    }

    fun notifyCellUpdateConsumed() {
        scheduleStore.notifyCellUpdateConsumed()
    }


    fun requestOptions(cellIndex: Int) {
        synchronized(scheduleStore) {
            val schedule = scheduleStore.scheduleLD.value ?: return
            if(cellIndex !in schedule.cells.indices) {
                return
            }

            scheduleStore.requestOptions( schedule.cells[cellIndex] )
        }
    }

    fun notifyCellOptionsSuggested() {
        scheduleStore.notifyCellOptionsSuggested()
    }

    fun notifyCellUpdateSuggested() {
        scheduleStore.notifyCellUpdateRequestConsumed()
    }


    private fun getSchedule(
        netMan: ConnectivityManager,
        callback: ConnectivityManager.NetworkCallback ) {

        mldLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repo.getSchedule()) {
                is GetScheduleResult.Success -> {
                    netMan.unregisterNetworkCallback(callback)

                    val scheduleModel = ScheduleModel.from(result.schedule)
                    scheduleStore.submitSchedule(scheduleModel)
                }

                is GetScheduleResult.Failed -> {
                    mldError.postValue(mapping.getFailedMessage)
                }
            }

            mldLoading.postValue(false)
        }
    }

    private fun updateSchedule(schedule: Schedule) {
        val result = repo.updateSchedule(schedule)

        if(result is UpdateScheduleResult.Success) {
            scheduleStore.submitUpdateScheduleSuccess()
            mldChanges.postValue(false)
        } else {
            mldError.postValue(mapping.updateFailedMessage)
        }
    }


    private fun startLoad(): Boolean {
        if(mldLoading.value == true) {
            return false
        }

        mldLoading.postValue(true)
        return true
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