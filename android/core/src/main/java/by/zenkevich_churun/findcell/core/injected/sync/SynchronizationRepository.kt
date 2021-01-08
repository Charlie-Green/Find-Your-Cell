package by.zenkevich_churun.findcell.core.injected.sync


/** Coordinates [SynchronizationScheduler] and [SynchronizedDataManager]. **/
interface SynchronizationRepository {
    fun sync(): Boolean
}