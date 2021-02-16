package by.zenkevich_churun.findcell.result.ui.general.container

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cpcontainer.vm.CoPrisonersContainerViewModel
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import javax.inject.Inject


class CoPrisonersGeneralViewModel @Inject constructor(
    syncRepo: SynchronizationRepository,
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersContainerViewModel(syncRepo, cpRepo, netTracker, changeRelationStore) {

    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CoPrisonersGeneralViewModel {

            val fact = CoPrisonersGeneralVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CoPrisonersGeneralViewModel::class.java)
        }
    }
}