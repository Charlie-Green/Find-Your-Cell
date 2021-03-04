package by.sviazen.result.ui.contact.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.result.ui.contact.model.GetCoPrisonerState
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CoPrisonerStateLDStorage @Inject constructor() {

    private val mldState = MutableLiveData<GetCoPrisonerState>().apply {
        value = GetCoPrisonerState.Idle
    }

    val stateLD: LiveData<GetCoPrisonerState>
        get() = mldState

    fun submit(state: GetCoPrisonerState) {
        if(mldState.value is GetCoPrisonerState.Idle &&
            state !is GetCoPrisonerState.Loading) {

            // UI has probably been dismissed. Don't trigger unwanted error messages.
            return
        }

        AndroidUtil.setOrPost(mldState, state)
    }
}