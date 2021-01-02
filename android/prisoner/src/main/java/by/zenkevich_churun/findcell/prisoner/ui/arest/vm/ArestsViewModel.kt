package by.zenkevich_churun.findcell.prisoner.ui.arest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.prisoner.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.prisoner.repo.arest.GetArestsResult
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.*
import by.zenkevich_churun.findcell.prisoner.ui.common.arest.CUArestStateHolder
import by.zenkevich_churun.findcell.prisoner.ui.common.arest.CreateOrUpdateArestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ArestsViewModel @Inject constructor(
    private val repo: ArestsRepository,
    private val netTracker: NetworkStateTracker,
    private val cuStateHolder: CUArestStateHolder
): ViewModel() {

    private val mldListState = MutableLiveData<ArestsListState>().apply {
        value = ArestsListState.Idle
    }
    private val mldDeleteState = MutableLiveData<DeleteArestsState>().apply {
        value = DeleteArestsState.Idle
    }
    private val mldOpenedArest = MutableLiveData<Arest?>()
    private val mldCheckable = MutableLiveData<Boolean>().apply {
        value = false
    }


    val listStateLD: LiveData<ArestsListState>
        get() = mldListState

    val addOrUpdateStateLD: LiveData<CreateOrUpdateArestState>
        get() = cuStateHolder.stateLD

    val openedArestLD: LiveData<Arest?>
        get() = mldOpenedArest

    val deleteStateLD: LiveData<DeleteArestsState>
        get() = mldDeleteState

    val loadingLD: LiveData<Boolean>
        = ArestLoadingMediatorLiveData(listStateLD, addOrUpdateStateLD, deleteStateLD)

    val checkableLD: LiveData<Boolean>
        get() = mldCheckable


    fun loadData(isRetrying: Boolean) {
        if( !isStateAppropriateToLoadData(isRetrying) ) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            mldListState.value = ArestsListState.NoInternet()
        }
        netTracker.doOnAvailable(this::loadDataInternal)
    }

    fun addArest(start: Long, end: Long) {
        val arests = this.arests ?: return

        if(!netTracker.isInternetAvailable) {
            cuStateHolder.submitState( CreateOrUpdateArestState.NoInternet() )
            return
        }

        cuStateHolder.submitState( CreateOrUpdateArestState.Loading )
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.addArest(start, end)
            applyResponse(arests, response.first, null, response.second)
        }
    }

    fun openSchedule(position: Int) {
        val arests = this.arests ?: return
        if(position in arests.indices) {
            mldOpenedArest.value = arests[position]
        }
    }

    fun notifyScheduleOpened() {
        mldOpenedArest.value = null
    }


    fun makeCheckable() {
        mldCheckable.value = true
    }

    fun delete() {
        val listState = mldListState.value as? ArestsListState.Loaded ?: return
        val checkedIds = listState.checkedIds

        if(!isStateAppropriateToDelete()) {
            return
        }
        if(!netTracker.isInternetAvailable) {
            mldDeleteState.value = DeleteArestsState.NoInternet()
            return
        }

        mldDeleteState.value = DeleteArestsState.InProgress
        viewModelScope.launch(Dispatchers.IO) {
            val deleteResult = repo.deleteArests(checkedIds)
            applyDeleteResult(deleteResult)
        }
    }

    fun cancelDelete() {
        mldCheckable.value = false
    }


    private val arests: List<Arest>?
        get() {
            val state = mldListState.value
            if(state !is ArestsListState.Loaded) {
                return null
            }

            return state.arests
        }

    private fun isStateAppropriateToLoadData(isRetrying: Boolean): Boolean {
        val state = mldListState.value ?: return true
        return when(state) {
            is ArestsListState.Idle         -> true
            is ArestsListState.NoInternet   -> false
            is ArestsListState.NetworkError -> isRetrying && state.notified
            else                            -> false
        }
    }

    private fun isStateAppropriateToDelete(): Boolean {
        val state = mldDeleteState.value ?: return true
        return when(state) {
            is DeleteArestsState.Idle         -> true
            is DeleteArestsState.InProgress   -> false
            is DeleteArestsState.NoInternet   -> state.notified
            is DeleteArestsState.NetworkError -> state.notified
            is DeleteArestsState.Success      -> state.notified
        }
    }


    private fun loadDataInternal() {
        mldListState.value = ArestsListState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.arestsList()
            applyResult(result)
        }
    }

    private fun applyResult(result: GetArestsResult) {
        when(result) {
            is GetArestsResult.Success -> {
                val newState = ArestsListState.Loaded(result.arests)
                mldListState.postValue(newState)
            }

            is GetArestsResult.NetworkError -> {
                mldListState.postValue( ArestsListState.NetworkError() )
            }

            is GetArestsResult.NotAuthorized -> {
                // TODO: Navigate to authorization screen.
                mldListState.postValue(ArestsListState.Idle)
            }
        }
    }

    private fun applyResponse(
        arests: List<Arest>,
        response: CreateOrUpdateArestResponse,
        oldPosition: Int?,
        newPosition: Int ) {

        when(response) {
            is CreateOrUpdateArestResponse.NetworkError -> {
                val state = CreateOrUpdateArestState.NetworkError(oldPosition == null)
                cuStateHolder.submitState(state)
            }

            is CreateOrUpdateArestResponse.ArestsIntersect -> {
                val intersectedArest = arests.find { a ->
                    a.id == response.intersectedId
                }
                val state = arestsIntersectState(intersectedArest, oldPosition == null)
                cuStateHolder.submitState(state)
            }

            is CreateOrUpdateArestResponse.Success -> {
                val state = oldPosition?.let {
                    CreateOrUpdateArestState.Updated(it, newPosition)
                } ?: CreateOrUpdateArestState.Created(newPosition)
                cuStateHolder.submitState(state)
            }
        }
    }

    private fun applyDeleteResult(deletedPositions: List<Int>?) {
        if(deletedPositions == null) {
            mldDeleteState.postValue( DeleteArestsState.NetworkError() )
            // Remain in Checkable mode.
            return
        }

        // Switch off the Checkable mode.
        mldCheckable.postValue(false)

        // Clean out the IDs that are now garbage:
        val listState = mldListState.value as? ArestsListState.Loaded
        listState?.checkedIds?.clear()

        // Publish the Success state:
        val state = DeleteArestsState.Success(
            deletedPositions.minByOrNull { it } ?: 0,
            deletedPositions.maxByOrNull { it } ?: 0
        )
        mldDeleteState.postValue(state)
    }


    private fun arestsIntersectState(
        intersectedArest: Arest?,
        operationCreate: Boolean
    ): CreateOrUpdateArestState {

        // This case is rare to impossible, yet it's implemented.
        // Ignore the add/update request.
        intersectedArest ?: return CreateOrUpdateArestState.Idle

        return CreateOrUpdateArestState.ArestsIntersectError(
            operationCreate,
            intersectedArest.start,
            intersectedArest.end
        )
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ArestsViewModel {

            val fact = ArestsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ArestsViewModel::class.java)
        }
    }
}