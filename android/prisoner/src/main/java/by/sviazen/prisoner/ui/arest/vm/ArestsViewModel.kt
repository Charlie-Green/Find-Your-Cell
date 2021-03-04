package by.sviazen.prisoner.ui.arest.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.domain.entity.Arest
import by.sviazen.domain.entity.Prisoner
import by.sviazen.prisoner.repo.arest.ArestsRepository
import by.sviazen.prisoner.repo.arest.GetArestsResult
import by.sviazen.prisoner.repo.profile.ProfileRepository
import by.sviazen.prisoner.ui.arest.state.*
import by.sviazen.prisoner.ui.common.arest.ArestLiveDatasHolder
import by.sviazen.prisoner.ui.common.arest.ArestsListState
import by.sviazen.prisoner.ui.common.arest.CreateOrUpdateArestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ArestsViewModel @Inject constructor(
    private val repo: ArestsRepository,
    private val profileRepo: ProfileRepository,
    private val netTracker: NetworkStateTracker,
    private val holder: ArestLiveDatasHolder
): ViewModel() {

    private val mldDeleteState = MutableLiveData<DeleteArestsState>().apply {
        value = DeleteArestsState.Idle
    }

    private val mldOpenedArest = MutableLiveData<Arest?>()

    private val mldCheckable = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val arestsObserver: Observer< List<Arest> >

    private var lastPrisonerId = Prisoner.INVALID_ID


    init {
        arestsObserver = Observer { arests ->
            holder.submitState( ArestsListState.Loaded(arests) )
        }
        repo.arestsLD.observeForever(arestsObserver)
    }


    override fun onCleared() {
        // Next time the screen is opened, the Arests are re-fetched.
        // Thus, the current arests list will no longer be used, so remove it.

        repo.clearArests()
        repo.arestsLD.removeObserver(arestsObserver)
    }


    val listStateLD: LiveData<ArestsListState>
        get() = holder.listStateLD

    val addOrUpdateStateLD: LiveData<CreateOrUpdateArestState>
        get() = holder.cuStateLD

    val openedArestLD: LiveData<Arest?>
        get() = mldOpenedArest

    val deleteStateLD: LiveData<DeleteArestsState>
        get() = mldDeleteState

    val loadingLD: LiveData<Boolean>
        = ArestLoadingMediatorLiveData(listStateLD, addOrUpdateStateLD, deleteStateLD)

    val checkableLD: LiveData<Boolean>
        get() = mldCheckable


    fun loadData(isRetrying: Boolean) {
        val prisonerId = profileRepo.prisonerLD.value?.id!! ?: return
        if(prisonerId != lastPrisonerId) {
            lastPrisonerId = prisonerId
            holder.submitState(ArestsListState.Idle)
        }

        if( !isStateAppropriateToLoadData(isRetrying) ) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            holder.submitState( ArestsListState.NoInternet() )
        }
        netTracker.doOnAvailable(this::loadDataInternal)
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
        val listState = listStateLD.value as? ArestsListState.Loaded ?: return
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
            val state = listStateLD.value
            if(state !is ArestsListState.Loaded) {
                return null
            }

            return state.arests
        }

    private fun isStateAppropriateToLoadData(isRetrying: Boolean): Boolean {
        val state = listStateLD.value ?: return true
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
        holder.submitState(ArestsListState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.arestsList()
            applyResult(result)
        }
    }

    private fun applyResult(result: GetArestsResult) {
        when(result) {
            is GetArestsResult.NetworkError -> {
                holder.submitState( ArestsListState.NetworkError() )
            }

            is GetArestsResult.NotAuthorized -> {
                // TODO: Navigate to authorization screen.
                holder.submitState(ArestsListState.Idle)
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
        val listState = holder.listStateLD.value as? ArestsListState.Loaded
        listState?.checkedIds?.clear()

        // Publish the Success state:
        val sortedPositions = deletedPositions.sorted()  // UI relies on it.
        val state = DeleteArestsState.Success(sortedPositions)
        mldDeleteState.postValue(state)
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