package by.sviazen.result.ui.shared.cppage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.result.ui.shared.cppage.model.ChangeRelationRequestState
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChangeRelationLiveDataStorage @Inject constructor() {

    private val mldState = MutableLiveData<ChangeRelationRequestState>()

    val stateLD: LiveData<ChangeRelationRequestState>
        get() = mldState

    fun submitState(state: ChangeRelationRequestState) {
        AndroidUtil.setOrPost(mldState, state)
    }
}