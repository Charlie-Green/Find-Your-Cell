package by.sviazen.prisoner.ui.common.arest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.domain.entity.Arest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArestLiveDatasHolder @Inject constructor() {

    private val mldListState = MediatorLiveData<ArestsListState>().apply {
        value = ArestsListState.Idle
    }

    private val mldCuState = MutableLiveData<CreateOrUpdateArestState>().apply {
        value = CreateOrUpdateArestState.Idle
    }

    private var listSource: LiveData< List<Arest> >? = null


    val listStateLD: LiveData<ArestsListState>
        get() = mldListState

    val cuStateLD: LiveData<CreateOrUpdateArestState>
        get() = mldCuState


    fun submitCU(state: CreateOrUpdateArestState) {
        AndroidUtil.setOrPost(mldCuState, state)
    }

    fun submitList(state: ArestsListState) {
        AndroidUtil.setOrPost(mldListState, state)
    }

    fun mediatorSubmitList(
        source: LiveData<List<Arest>>,
        mapper: (List<Arest>) -> ArestsListState ) {

        listSource?.also { mldListState.removeSource(it) }
        listSource = source
        mldListState.addSource(source) { newList ->
            val newState = mapper(newList)
            AndroidUtil.setOrPost(mldListState, newState)
        }
    }

    fun clear() {
        listSource?.also { mldListState.removeSource(it) }
        listSource = null
    }
}