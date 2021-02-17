package by.zenkevich_churun.findcell.result.ui.shared.cppage.vm

import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cppage.model.ChangeRelationRequestState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/** Implements some functionality common for both
  * [CoPrisonersPage]s [ViewModel]s. **/
abstract class CoPrisonersPageViewModel(
    protected val cpRepo: CoPrisonersRepository,
    protected val changeRelationStore: ChangeRelationLiveDataStorage,
    protected val netTracker: NetworkStateTracker
): ViewModel() {
    // ================================================================================
    // Fields:

    private val mldExpandedPosition = MutableLiveData<Int>().apply {
        value = -1
    }

    private val mldData by lazy {
        createDataMLD()
    }


    // ================================================================================
    // Properties/Methods (Public):

    /** Emits position of item within [CoPrisoner]s [List] whose state is expanded,
      * meaning options for this item are visible to the user. **/
    val expandedPositionLD: LiveData<Int>
        get() = mldExpandedPosition

    /** Emits a pair (C; p) such that:
      * - C is the list of [CoPrisoner]s to be shown to the user.
      * - p: position of specific item to update.
      *      If negative, the entire list have to be updated. **/
    val dataLD: LiveData< Pair<List<CoPrisoner>, Int> >
        get() = mldData


    fun swapPositionExpandedStatus(position: Int) {
        mldExpandedPosition.value =
            if(mldExpandedPosition.value == position) -1
            else position
    }

    fun onStop() {
        viewModelScope.launch(Dispatchers.Default) {
            mldData.sort()
        }
    }


    // ================================================================================
    // Properties/Methods (Protected):

    protected open val changeRelationRequestStateLD: LiveData<ChangeRelationRequestState>
        get() = changeRelationStore.stateLD

    protected fun connect(position: Int) {
        sendRequest(position) { cpId ->
            val result = cpRepo.sendConnectRequest(cpId)
            applyConnectRequestResult(cpId, result)
        }
    }

    protected fun disconnect(position: Int) {
        sendRequest(position) { cpId ->
            val result = cpRepo.cancelConnectRequest(cpId)
            applyConnectRequestResult(cpId, result)
        }
    }


    // ================================================================================
    // Properties/Methods (Private):

    private fun sendRequest(
        position: Int,
        asyncPart: (cpId: Int) -> Unit ) {

        val cp = coPrisonerAt(position) ?: return
        if(changeRelationRequestStateLD.value is ChangeRelationRequestState.Sending ||
            !netTracker.isInternetAvailable ) {

            return
        }

        // Do not update the entire list on next emission.
        // Update only this position instead.
        mldData.setUpdatedPosition(position)

        changeRelationStore.submitState(ChangeRelationRequestState.Sending)

        viewModelScope.launch(Dispatchers.IO) {
            asyncPart(cp.id)
        }
    }

    private fun applyConnectRequestResult(
        coPrisonerId: Int,
        newRelation: CoPrisoner.Relation? ) {

        if(newRelation == null) {
            changeRelationStore.submitState(ChangeRelationRequestState.NetworkError())
            return
        }

        val position = mldData.value?.first?.indexOfFirst { cp ->
            cp.id == coPrisonerId
        } ?: -1

        val state =
            if(position >= 0) ChangeRelationRequestState.Success(position)
            else ChangeRelationRequestState.NetworkError()
        changeRelationStore.submitState(state)
    }


    private fun coPrisonerAt(position: Int): CoPrisoner? {
        val list = mldData.value?.first ?: return null
        if(position !in list.indices) {
            return null
        }

        return list[position]
    }


    private fun createDataMLD(): CoPrisonersPageLiveData {
        val dataSource = getDataSource(viewModelScope)
        return CoPrisonersPageLiveData(
            dataSource, viewModelScope, dataComparator )
    }


    protected open val dataComparator: Comparator<CoPrisoner>?
        get() = null

    protected abstract fun getDataSource(
        scope: CoroutineScope
    ): LiveData< List<CoPrisoner> >
}