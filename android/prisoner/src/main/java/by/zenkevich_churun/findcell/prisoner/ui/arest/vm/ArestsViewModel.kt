package by.zenkevich_churun.findcell.prisoner.ui.arest.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.prisoner.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.prisoner.repo.arest.GetArestsResult
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.ArestsListState
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.CreateOrUpdateArestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ArestsViewModel @Inject constructor(
    private val repo: ArestsRepository,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    private val mldListState = MutableLiveData<ArestsListState>().apply {
        value = ArestsListState.Idle
    }
    private val mldAddState = MutableLiveData<CreateOrUpdateArestState>().apply {
        value = CreateOrUpdateArestState.Idle
    }
    private val mldOpenedArest = MutableLiveData<Arest?>()
    private val mldCheckable = MutableLiveData<Boolean>().apply {
        value = false
    }


    val listStateLD: LiveData<ArestsListState>
        get() = mldListState

    val addOrUpdateStateLD: LiveData<CreateOrUpdateArestState>
        get() = mldAddState

    val openedArestLD: LiveData<Arest?>
        get() = mldOpenedArest

    val loadingLD: LiveData<Boolean>
        = ArestLoadingMediatorLiveData(listStateLD, addOrUpdateStateLD)

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
            mldAddState.value = CreateOrUpdateArestState.NoInternet()
            return
        }

        mldAddState.value = CreateOrUpdateArestState.Loading
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
        // TODO
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

        Log.v("CharlieDebug", "Response = ${response.javaClass.simpleName}, position = $newPosition")

        when(response) {
            is CreateOrUpdateArestResponse.NetworkError -> {
                val state = CreateOrUpdateArestState.NetworkError(oldPosition == null)
                mldAddState.postValue(state)
            }

            is CreateOrUpdateArestResponse.ArestsIntersect -> {
                val intersectedArest = arests.find { a ->
                    a.id == response.intersectedId
                }
                val state = arestsIntersectState(intersectedArest, oldPosition == null)
                mldAddState.postValue(state)
            }

            is CreateOrUpdateArestResponse.Success -> {
                val state = oldPosition?.let {
                    CreateOrUpdateArestState.Updated(it, newPosition)
                } ?: CreateOrUpdateArestState.Created(newPosition)
                mldAddState.postValue(state)
            }
        }
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