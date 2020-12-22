package by.zenkevich_churun.findcell.prisoner.ui.common.interrupt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InterruptLiveDataStorage @Inject constructor() {

    private val mldState = MutableLiveData<EditInterruptState>().apply {
        value = EditInterruptState.NotRequested
    }

    val stateLD: LiveData<EditInterruptState>
        get() = mldState


    fun interrupt(source: Int, dest: Int) {
        val state = mldState.value as? EditInterruptState.NotRequested ?: return
        mldState.value = EditInterruptState.Asking(source, dest)
    }

    fun confirm() {
        val state = mldState.value as? EditInterruptState.Asking ?: return
        mldState.value = EditInterruptState.Confirmed(state.source, state.dest)
    }

    fun decline() {
        val state = mldState.value as? EditInterruptState.Asking ?: return
        mldState.value = EditInterruptState.NotRequested
    }

    fun notifyConfirmationConsumed() {
        val state = mldState.value as? EditInterruptState.Confirmed ?: return
        mldState.value = EditInterruptState.NotRequested
    }
}