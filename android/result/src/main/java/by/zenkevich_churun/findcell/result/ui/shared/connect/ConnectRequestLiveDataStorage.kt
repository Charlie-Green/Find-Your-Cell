package by.zenkevich_churun.findcell.result.ui.shared.connect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConnectRequestLiveDataStorage @Inject constructor() {

    private val mldConnectRequestState = MutableLiveData<ConnectRequestState>()

    val stateLD: LiveData<ConnectRequestState>
        get() = mldConnectRequestState

    fun submitState(state: ConnectRequestState) {
        AndroidUtil.setOrPost(mldConnectRequestState, state)
    }

}