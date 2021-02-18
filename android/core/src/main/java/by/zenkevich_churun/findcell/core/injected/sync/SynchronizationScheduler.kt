package by.zenkevich_churun.findcell.core.injected.sync


/** Part of business logic to define when to start sync,
  * modify sync time due to events going on in the app,
  * registering and cancelling appropriate system triggers. **/
interface SynchronizationScheduler {
    val lastSucessfulSyncTime: Long
    val isTimeToSync: Boolean
    fun scheduleFirstSync()
    fun notifyArestUpdated()
    fun notifySyncRan()
    fun notifySyncFinished(success: Boolean)
    fun cancelSyncs()
}