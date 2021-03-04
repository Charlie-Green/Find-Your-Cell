package by.sviazen.core.injected.sync


/** Coordinator object between network APIs and local database
  * to perform synchronization. **/
interface SynchronizedDataManager {
    fun sync()
    fun clear()
}