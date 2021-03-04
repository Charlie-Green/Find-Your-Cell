package by.sviazen.core.injected.sync


/** Ways [SynchronizationRepository] may react to sync requests. **/
enum class SyncResponse {
    SUCCESS,
    ERROR,
    IGNORED
}