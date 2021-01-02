package by.zenkevich_churun.findcell.prisoner.ui.arest.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.*
import by.zenkevich_churun.findcell.prisoner.ui.common.arest.CreateOrUpdateArestState


internal class ArestLoadingMediatorLiveData(
    listStateLD: LiveData<ArestsListState>,
    addStateLD: LiveData<CreateOrUpdateArestState>,
    deleteStateLD: LiveData<DeleteArestsState>
): MediatorLiveData<Boolean>() {

    private var listLoading   = false
    private var addLoading    = false
    private var deleteLoading = false


    init {
        listen(listStateLD) { state ->
            listLoading = (state is ArestsListState.Loading)
        }
        listen(addStateLD) { state ->
            addLoading = (state is CreateOrUpdateArestState.Loading)
        }
        listen(deleteStateLD) { state ->
            deleteLoading = (state is DeleteArestsState.InProgress)
        }
    }


    private inline fun <StateType> listen(
        ld: LiveData<StateType>,
        crossinline applyState: (state: StateType) -> Unit ) {

        addSource(ld, Observer { state ->
            applyState(state)
            updateValue()
        })
    }

    private fun updateValue() {
        postValue(listLoading || addLoading || deleteLoading)
    }
}