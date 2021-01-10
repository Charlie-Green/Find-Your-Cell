package by.zenkevich_churun.findcell.result.ui.suggest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import by.zenkevich_churun.findcell.core.util.ld.EmissionIgnoringMediatorLiveData
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.result.ui.shared.cps.CoPrisonersPageViewModel
import javax.inject.Inject


class SuggestedCoPrisonersViewModel @Inject constructor(
    private val repo: CoPrisonersRepository,
    private val netTracker: NetworkStateTracker
): CoPrisonersPageViewModel() {

    private val mldData by lazy {
        dataLiveData()
    }


    override val dataLD: LiveData< List<CoPrisoner> >
        get() = mldData.apply {
            // Ensure the latest value after a configuration change:
            setIgnoredValue()
        }


    fun sendConnectRequest(position: Int) {
        val cp = coPrisonerAt(position) ?: return
        if(!netTracker.isInternetAvailable) {
            // TODO
            return
        }
    }


    private fun dataLiveData(): EmissionIgnoringMediatorLiveData< List<CoPrisoner> > {
        val source = repo.suggestedLD(viewModelScope)
        return EmissionIgnoringMediatorLiveData(source)
    }

    private fun coPrisonerAt(position: Int): CoPrisoner? {
        val list = mldData.value ?: return null
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