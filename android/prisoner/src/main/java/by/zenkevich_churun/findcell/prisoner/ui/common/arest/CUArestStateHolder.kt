package by.zenkevich_churun.findcell.prisoner.ui.common.arest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CUArestStateHolder @Inject constructor() {

    private val mld = MutableLiveData<CreateOrUpdateArestState>()


    val stateLD: LiveData<CreateOrUpdateArestState>
        get() = mld

    fun submitState(state: CreateOrUpdateArestState) {
        mld.postValue(state)
    }
}