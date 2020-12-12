package by.zenkevich_churun.findcell.prisoner.ui.common.interrupt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InterruptLiveDataStorage @Inject constructor() {

    private val mldState = MutableLiveData<EditInterruptState>().apply {
        value = EditInterruptState.NOT_REQUESTED
    }

    val stateLD: LiveData<EditInterruptState>
        get() = mldState


    fun requestResult()
        = transmith(EditInterruptState.NOT_REQUESTED, EditInterruptState.ASKING)

    fun confirmRequest()
        = transmith(EditInterruptState.ASKING, EditInterruptState.CONFIRMED)

    fun declineRequest()
        = transmith(EditInterruptState.ASKING, EditInterruptState.NOT_REQUESTED)

    fun notifyConfirmationConsumed()
        = transmith(EditInterruptState.CONFIRMED, EditInterruptState.NOT_REQUESTED)


    private fun transmith(
        expectedCurrentState: EditInterruptState,
        nextState: EditInterruptState) {

        if(mldState.value == expectedCurrentState) {
            mldState.value = nextState
        }
    }
}