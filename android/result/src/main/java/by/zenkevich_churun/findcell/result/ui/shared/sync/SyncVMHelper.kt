package by.zenkevich_churun.findcell.result.ui.shared.sync

import android.util.Log
import by.zenkevich_churun.findcell.core.injected.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.core.injected.web.NetworkStateTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/** Helper object shared between multiple [ViewModel]s
  * and implementing some of their common functionality related to synchronization. **/
internal object SyncVMHelper {
    private var syncTriggered = false


    fun triggerSync(
        repo: SynchronizationRepository,
        netTracker: NetworkStateTracker,
        scope: CoroutineScope) {

        if(syncTriggered) {
            return
        }
        Log.v("CharlieDebug", "Starting sync")
        syncTriggered = true

        netTracker.doOnAvailable {
            scope.launch(Dispatchers.IO) {
                repo.sync()
            }
        }
    }
}