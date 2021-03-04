package by.sviazen.result.ui.general.suggest

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


class SuggestedCoPrisonersViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {
    // ===============================================================================

    override fun dataSource()
        = cpRepo.suggestedLD

    override val dataComparator: Comparator<CoPrisoner>?
        get() = SuggestedFirstComparator()

    fun sendConnectRequest(position: Int)
        = connect(position, this::requestSentMessage)

    fun cancelConnectRequest(position: Int)
        = disconnect(position, this::requestCanceledMessage)


    // ===============================================================================

    private fun requestSentMessage(cp: CoPrisoner): String {
        if(cp.relation != CoPrisoner.Relation.OUTCOMING_REQUEST) {
            return appContext.getString(R.string.change_relation_success_general)
        }

        return appContext.getString(
            R.string.change_relation_success_requested,
            cp.name
        )
    }

    private fun requestCanceledMessage(cp: CoPrisoner): String {
        if(cp.relation != CoPrisoner.Relation.SUGGESTED) {
            return appContext.getString(R.string.change_relation_success_general)
        }
        return appContext.getString(R.string.change_relation_success_canceled)
    }


    // ===============================================================================

    private class SuggestedFirstComparator: Comparator<CoPrisoner> {

        override fun compare(
            p1: CoPrisoner,
            p2: CoPrisoner
        ): Int = priorityOf(p1.relation) - priorityOf(p2.relation)


        private fun priorityOf(relation: CoPrisoner.Relation): Int {
            if(relation == CoPrisoner.Relation.SUGGESTED) {
                return -1
            }
            return 0
        }
    }


    // ===============================================================================

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