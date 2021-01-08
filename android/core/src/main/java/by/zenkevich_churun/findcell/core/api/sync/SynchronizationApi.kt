package by.zenkevich_churun.findcell.core.api.sync

import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo


interface SynchronizationApi {

    fun fetchData(
        prisonerId: Int,
        passwordHash: ByteArray
    ): SynchronizedPojo
}