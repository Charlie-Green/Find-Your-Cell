package by.zenkevich_churun.findcell.prisoner.ui.common.arest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArestLiveDatasHolder @Inject constructor() {

    private val mldListState = MutableLiveData<ArestsListState>().apply {
        value = ArestsListState.Idle
    }

    private val mldCuState = MutableLiveData<CreateOrUpdateArestState>().apply {
        value = CreateOrUpdateArestState.Idle
    }


    val listStateLD: LiveData<ArestsListState>
        get() = mldListState

    val cuStateLD: LiveData<CreateOrUpdateArestState>
        get() = mldCuState


    fun submitState(state: ArestsListState) {
        mldListState.postValue(state)
    }

    fun submitState(state: CreateOrUpdateArestState) {
        mldCuState.postValue(state)
    }
}