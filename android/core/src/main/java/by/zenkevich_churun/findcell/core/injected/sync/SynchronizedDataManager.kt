package by.zenkevich_churun.findcell.core.injected.sync


/** Coordinator object between network APIs and local database
  * to perform synchronization. **/
interface SynchronizedDataManager {
    // TODO: Add getter for the current cached value.
    fun sync()
}