package by.zenkevich_churun.findcell.result.ui.contact.vm

import android.content.Context
import androidx.lifecycle.*
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import javax.inject.Inject


class CoPrisonerContactsViewModel @Inject constructor(): ViewModel() {

    private val mldPrisoner = MutableLiveData<Prisoner>()

    val prisonerLD: LiveData<Prisoner>
        get() = mldPrisoner

    fun loadPrisoner(id: Int) {
        // TODO
    }


    companion object {

        fun get(
            appContext: Context,
            storeOwner: ViewModelStoreOwner
        ): CoPrisonerContactsViewModel {

            val fact = CoPrisonerContactsVMFactory.get(appContext)
            val provider = ViewModelProvider(storeOwner, fact)
            return provider.get(CoPrisonerContactsViewModel::class.java)
        }
    }
}