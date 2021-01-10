package by.zenkevich_churun.findcell.result.ui.connect.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPageViewModel
import javax.inject.Inject


class ConnectedCoPrisonersViewModel @Inject constructor(
    private val repo: CoPrisonersRepository
): CoPrisonersPageViewModel() {

    override val dataLD: LiveData< List<CoPrisoner> >
        get() = repo.connectedLD(viewModelScope)


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