package by.zenkevich_churun.findcell.core.injected.sync


sealed class SyncState {
    object InProgress: SyncState()

    class NotSyncing(
        val lastSuccessTime: Long,
        val errorFlag: Boolean
    ): SyncState()
}