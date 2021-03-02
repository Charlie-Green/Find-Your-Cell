package by.zenkevich_churun.findcell.core.api.sync

import by.zenkevich_churun.findcell.domain.contract.sync.SynchronizedPojo


interface SynchronizationApi {

    fun fetchData(
        prisonerId: Int,
        passwordHash: ByteArray
    ): SynchronizedPojo
}