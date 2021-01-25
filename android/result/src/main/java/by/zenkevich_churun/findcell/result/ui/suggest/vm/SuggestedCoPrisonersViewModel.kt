package by.zenkevich_churun.findcell.result.ui.suggest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.connect.ConnectRequestState
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class SuggestedCoPrisonersViewModel @Inject constructor(
    private val repo: CoPrisonersRepository,
    private val netTracker: NetworkStateTracker,
    private val connectRequestStore: ConnectRequestLiveDataStorage
): CoPrisonersPageViewModel() {

    val connectRequestStateLD: LiveData<ConnectRequestState>
        get() = connectRequestStore.stateLD


    override val dataSource: LiveData<List<CoPrisoner>>
        get() = repo.suggestedLD(viewModelScope)


    fun sendConnectRequest(position: Int) {
        val cp = coPrisonerAt(position) ?: return
        if(connectRequestStateLD.value is ConnectRequestState.Sending ||
            !netTracker.isInternetAvailable ) {

            return
        }

        // Do not update the entire list on next emission.
        // UI will be given position of the specific item to update.
        updateDataEntirely = false

        connectRequestStore.submitState(ConnectRequestState.Sending)

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.sendConnectRequest(cp.id)
            applyConnectRequestResult(cp.id, result)
        }
    }


    private fun applyConnectRequestResult(
        coPrisonerId: Int,
        newRelation: CoPrisoner.Relation? ) {

        if(newRelation == null) {
            connectRequestStore.submitState(ConnectRequestState.NetworkError())
            return
        }

        val position = mldData.value?.first?.indexOfFirst { cp ->
            cp.id == coPrisonerId
        } ?: -1

        val state =
            if(position >= 0) ConnectRequestState.Success(position)
            else ConnectRequestState.NetworkError()
        connectRequestStore.submitState(state)
    }


    private fun coPrisonerAt(position: Int): CoPrisoner? {
        val list = mldData.value?.first ?: return null
        if(position !in list.indices) {
            return null
        }

        return list[position]
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): SuggestedCoPrisonersViewModel {

            val fact = SuggestedCoPrisonersVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(SuggestedCoPrisonersViewModel::class.java)
        }
    }
}