package by.sviazen.core.api.sync

import by.sviazen.domain.contract.sync.SynchronizedPojo


interface SynchronizationApi {

    fun fetchData(
        prisonerId: Int,
        passwordHash: ByteArray
    ): SynchronizedPojo
}