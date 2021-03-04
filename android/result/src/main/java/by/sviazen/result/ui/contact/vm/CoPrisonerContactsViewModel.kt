package by.sviazen.result.ui.contact.vm

import android.content.Context
import androidx.lifecycle.*
import by.sviazen.core.injected.cp.CoPrisonersRepository
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.domain.contract.cp.GetCoPrisonerResponse
import by.sviazen.result.ui.contact.model.GetCoPrisonerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CoPrisonerContactsViewModel @Inject constructor(
    private val repo: CoPrisonersRepository,
    private val stateStore: CoPrisonerStateLDStorage,
    private val netTracker: NetworkStateTracker
): ViewModel() {

    override fun onCleared() {
        stateStore.submit(GetCoPrisonerState.Idle)
    }


    val stateLD: LiveData<GetCoPrisonerState>
        get() = stateStore.stateLD

    fun loadPrisoner(id: Int, name: String) {
        if(!shouldLoadPrisoner(id)) {
            return
        }

        if(!netTracker.isInternetAvailable) {
            stateStore.submit(GetCoPrisonerState.Error.NoInternet())
            return
        }

        stateStore.submit(GetCoPrisonerState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getPrisoner(id)
            val state = responseToState(id, name, response)
            stateStore.submit(state)
        }
    }


    private fun shouldLoadPrisoner(id: Int): Boolean {
        val state = stateLD.value ?: GetCoPrisonerState.Idle

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
        name: String,
        response: GetCoPrisonerResponse
    ): GetCoPrisonerState = when(response) {

        is GetCoPrisonerResponse.Success ->
            GetCoPrisonerState.Success(id, name, response.contacts, response.info)

        is GetCoPrisonerResponse.NotConnected ->
            GetCoPrisonerState.Error.NotConnected(name)

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