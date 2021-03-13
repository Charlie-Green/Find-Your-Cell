package by.sviazen.result.ui.request.outcome

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.repo.cp.CoPrisonersRepository
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.R
import by.sviazen.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.sviazen.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class OutcomingRequestsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {

    override fun dataSource()
        = cpRepo.outcomingRequestsLD


    fun cancelRequest(position: Int)
        = disconnect(position, this::requestCanceledMessage)


    private fun requestCanceledMessage(cp: CoPrisoner): String {
        if(cp.relation != CoPrisoner.Relation.REQUEST_DECLINED) {
            return appContext.getString(R.string.change_relation_success_general)
        }
        return appContext.getString(R.string.change_relation_success_canceled)
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): OutcomingRequestsViewModel {

            val fact = OutcomingRequestsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(OutcomingRequestsViewModel::class.java)
        }
    }
}