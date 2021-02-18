package by.zenkevich_churun.findcell.result.ui.general.connect

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import by.zenkevich_churun.findcell.core.injected.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.ChangeRelationLiveDataStorage
import by.zenkevich_churun.findcell.result.ui.shared.cppage.vm.CoPrisonersPageViewModel
import javax.inject.Inject


class ConnectedCoPrisonersViewModel @Inject constructor(
    cpRepo: CoPrisonersRepository,
    netTracker: NetworkStateTracker,
    changeRelationStore: ChangeRelationLiveDataStorage
): CoPrisonersPageViewModel(cpRepo, changeRelationStore, netTracker) {

    override fun dataSource()
        = cpRepo.connectedLD


    fun breakConnection(position: Int)
        = disconnect(position)


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): ConnectedCoPrisonersViewModel {

            val fact = ConnectedCoPrisonersVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(ConnectedCoPrisonersViewModel::class.java)
        }
    }
}