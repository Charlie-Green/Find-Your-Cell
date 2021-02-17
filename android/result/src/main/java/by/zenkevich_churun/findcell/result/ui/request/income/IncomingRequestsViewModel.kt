package by.zenkevich_churun.findcell.result.ui.request.income

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class IncomingRequestsViewModel @Inject constructor(
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {
    // ==============================================================================

    override fun getDataSource(
        scope: CoroutineScope
    ) = cpRepo.incomingRequestsLD(viewModelScope)

    override val dataComparator: Comparator<CoPrisoner>?
        get() = IncomingRequestsFirstComparator()


    fun confirmRequest(position: Int)
        = connect(position)

    fun declineRequest(position: Int)
        = disconnect(position)


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