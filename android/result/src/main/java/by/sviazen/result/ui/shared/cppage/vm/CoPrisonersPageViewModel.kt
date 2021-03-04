package by.sviazen.result.ui.shared.cppage.vm

import androidx.lifecycle.*
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.injected.cp.CoPrisonersRepository
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.ui.shared.cppage.model.ChangeRelationRequestState
import kotlinx.coroutines.Dispatchers
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


    // ================================================================================
    // Properties/Methods (Protected):

    protected open val changeRelationRequestStateLD: LiveData<ChangeRelationRequestState>
        get() = changeRelationStore.stateLD

    protected fun connect(
        position: Int,
        createSuccessMessage: (CoPrisoner) -> String
    ) = sendRequest(position) { cpId ->
        val result = cpRepo.connect(cpId)
        applyConnectRequestResult(cpId, result, createSuccessMessage)
    }

    protected fun disconnect(
        position: Int,
        createSuccessMessage: (CoPrisoner) -> String
    ) = sendRequest(position) { cpId ->
        val result = cpRepo.disconnect(cpId)
        applyConnectRequestResult(cpId, result, createSuccessMessage)
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
        newRelation: CoPrisoner.Relation?,
        createSuccessMessage: (CoPrisoner) -> String ) {

        if(newRelation == null) {
            changeRelationStore.submitState(ChangeRelationRequestState.NetworkError())
            return
        }

        val coPrisoners = mldData.value?.first
        val position = coPrisoners?.indexOfFirst { cp ->
            cp.id == coPrisonerId
        }.let { position ->
            if(position == null || position !in coPrisoners!!.indices) -1
            else position
        }

        val state = if(position >= 0) {
            val legacyCoPrisoner = coPrisoners!![position]
            val correctCoPrisoner =
                CoPrisonerWithUpdatedRelation.wrap(legacyCoPrisoner, newRelation)
            val message = createSuccessMessage(correctCoPrisoner)
            ChangeRelationRequestState.Success(position, message)
        } else {
            ChangeRelationRequestState.NetworkError()
        }

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
        val dataSource = dataSource()
        return CoPrisonersPageLiveData(
            dataSource, viewModelScope, dataComparator )
    }


    protected open val dataComparator: Comparator<CoPrisoner>?
        get() = null

    protected abstract fun dataSource(): LiveData< out List<CoPrisoner> >
}