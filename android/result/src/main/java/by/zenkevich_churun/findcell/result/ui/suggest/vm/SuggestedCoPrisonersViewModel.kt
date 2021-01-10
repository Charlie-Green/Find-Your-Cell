package by.zenkevich_churun.findcell.result.ui.suggest.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import javax.inject.Inject


class SuggestedCoPrisonersViewModel @Inject constructor(
    private val repo: CoPrisonersRepository
): ViewModel() {

    val dataLD: LiveData< List<CoPrisoner> >
        get() = repo.suggestedLD(viewModelScope)


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