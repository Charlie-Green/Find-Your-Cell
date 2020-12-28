package by.zenkevich_churun.findcell.prisoner.ui.arest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.Arest
import by.zenkevich_churun.findcell.prisoner.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.prisoner.repo.arest.GetArestsResult
import by.zenkevich_churun.findcell.prisoner.ui.arest.state.ArestsListState
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
    private val mldOpenedArest = MutableLiveData<Arest?>()


    val listStateLD: LiveData<ArestsListState>
        get() = mldListState

    val openedArestLD: LiveData<Arest?>
        get() = mldOpenedArest


    fun loadData() {
        if(mldListState.value !is ArestsListState.Idle) {
            return
        }
        if(!netTracker.isInternetAvailable) {
            mldListState.value = ArestsListState.NoInternet()
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


    private val arests: List<Arest>?
        get() {
            val state = mldListState.value
            if(state !is ArestsListState.Loaded) {
                return null
            }

            return state.arests
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