package by.sviazen.result.ui.request.income

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import by.sviazen.core.injected.web.NetworkStateTracker
import by.sviazen.core.injected.cp.CoPrisonersRepository
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.R
import by.sviazen.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.sviazen.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class IncomingRequestsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {
    // ==============================================================================

    override fun dataSource()
        = cpRepo.incomingRequestsLD

    override val dataComparator: Comparator<CoPrisoner>?
        get() = IncomingRequestsFirstComparator()


    fun confirmRequest(position: Int)
        = connect(position, this::requestConfirmedMessage)

    fun declineRequest(position: Int)
        = disconnect(position, this::requestDeclinedMessage)


    // ==============================================================================

    private fun requestConfirmedMessage(cp: CoPrisoner): String {
        if(cp.relation != CoPrisoner.Relation.CONNECTED) {
            return appContext.getString(R.string.change_relation_success_general)
        }

        return appContext.getString(
            R.string.change_relation_success_connected,
            cp.name
        )
    }

    private fun requestDeclinedMessage(cp: CoPrisoner): String {
        if(cp.relation != CoPrisoner.Relation.REQUEST_DECLINED) {
            return appContext.getString(R.string.change_relation_success_general)
        }

        return appContext.getString(
            R.string.change_relation_success_declined,
            cp.name
        )
    }


    // ==============================================================================

    private class IncomingRequestsFirstComparator: Comparator<CoPrisoner> {

        override fun compare(
            p1: CoPrisoner,
            p2: CoPrisoner
        ): Int = priorityOf(p1.relation) - priorityOf(p2.relation)


        private fun priorityOf(relation: CoPrisoner.Relation): Int {
            if(relation == CoPrisoner.Relation.INCOMING_REQUEST) {
                return -1
            }
            return 0
        }
    }


    // ==============================================================================

    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): IncomingRequestsViewModel {

            val fact = IncomingRequestsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(IncomingRequestsViewModel::class.java)
        }
    }
}