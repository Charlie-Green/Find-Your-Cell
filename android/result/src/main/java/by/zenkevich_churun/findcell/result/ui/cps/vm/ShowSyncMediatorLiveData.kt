package by.zenkevich_churun.findcell.result.ui.cps.vm

import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.core.injected.sync.SyncState
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.result.repo.cp.CoPrisonersRepository
import kotlinx.coroutines.CoroutineScope


internal class ShowSyncMediatorLiveData(
    syncRepo: SynchronizationRepository,
    cpRepo: CoPrisonersRepository,
    scope: CoroutineScope
): MediatorLiveData<Boolean>() {

    private var syncing = false
    private var suggested = false
    private var connected = false


    init {
        addSource(syncRepo.syncStateLD) { state ->
            syncing = (state is SyncState.InProgress)
            updateValue()
        }

        addSource(cpRepo.suggestedLD(scope)) { list ->
            suggested = !list.isEmpty()
            updateValue()
        }

        addSource(cpRepo.connectedLD(scope)) { list ->
            connected = !list.isEmpty()
            updateValue()
        }
    }


    private fun updateValue() {
        val newValue = syncing && !suggested && !connected
        AndroidUtil.setOrPost(this, newValue)
    }
}