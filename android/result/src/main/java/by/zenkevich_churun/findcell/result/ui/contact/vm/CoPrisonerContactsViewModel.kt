package by.zenkevich_churun.findcell.result.ui.contact.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.entity.response.GetCoPrisonerResponse
import by.zenkevich_churun.findcell.result.ui.contact.model.GetCoPrisonerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CoPrisonerContactsViewModel @Inject constructor(
    private val repo: CoPrisonersRepository
): ViewModel() {

    private val mldState = MutableLiveData<GetCoPrisonerState>().apply {
        value = GetCoPrisonerState.Idle
    }

    val stateLD: LiveData<GetCoPrisonerState>
        get() = mldState

    fun loadPrisoner(id: Int) {
        if(!shouldLoadPrisoner(id)) {
            return
        }

        mldState.postValue(GetCoPrisonerState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getPrisoner(id)
            val state = responseToState(id, response)
            mldState.postValue(state)
        }
    }


    private fun shouldLoadPrisoner(id: Int): Boolean {
        val state = mldState.value!!

        if(state is GetCoPrisonerState.Error) {
            return state.dialogConsumed && state.containerConsumed
        }
        if(state is GetCoPrisonerState.Success) {
            return state.id != id
        }

        return true
    }

    private fun responseToState(
        id: Int,
        response: GetCoPrisonerResponse
    ): GetCoPrisonerState = when(response) {

        is GetCoPrisonerResponse.Success ->
            GetCoPrisonerState.Success(id, response.contacts, response.info)

        is GetCoPrisonerResponse.NotConnected ->
            GetCoPrisonerState.Error.NotConnected()

        is GetCoPrisonerResponse.NetworkError ->
            GetCoPrisonerState.Error.Network()
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CoPrisonerContactsViewModel {

            val fact = CoPrisonerContactsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CoPrisonerContactsViewModel::class.java)
        }
    }
}