package by.sviazen.result.ui.shared.cpcontainer.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import by.sviazen.core.injected.sync.SyncState
import by.sviazen.core.repo.sync.SynchronizationRepository
import by.sviazen.core.util.android.AndroidUtil
import by.sviazen.domain.entity.CoPrisoner


internal class ShowSyncMediatorLiveData(
    syncRepo: SynchronizationRepository,
    source1: LiveData< out List<CoPrisoner> >,
    source2: LiveData< out List<CoPrisoner> >
): MediatorLiveData<Boolean>() {

    private var syncing = false
    private var dataShowing = false


    init {
        addSource(syncRepo.syncStateLD) { state ->
            syncing = (state is SyncState.InProgress)
            updateValue()
        }

        addSource(source1) { list ->
            dataShowing = dataShowing || !list.isEmpty()
            updateValue()
        }

        addSource(source2) { list ->
            dataShowing = dataShowing || !list.isEmpty()
            updateValue()
        }
    }


    private fun updateValue() {
        val newValue = syncing && !dataShowing
        AndroidUtil.setOrPost(this, newValue)
    }
}