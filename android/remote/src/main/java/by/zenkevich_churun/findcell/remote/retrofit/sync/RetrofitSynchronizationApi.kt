package by.zenkevich_churun.findcell.remote.retrofit.sync

import by.zenkevich_churun.findcell.core.api.sync.SynchronizationApi
import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitSynchronizationApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): SynchronizationApi {

    override fun fetchData(
        prisonerId: Int,
        passwordHash: ByteArray
    ): SynchronizedPojo {

        val passBase64 = Base64Util.encode(passwordHash)

        val service = retrofitHolder
            .retrofit
            .create(SynchronizationService::class.java)

        val response = service
            .fetchData(1, prisonerId, passBase64)
            .execute()

        return response.body()
            ?: throw IOException("No response body. Code is ${response.code()}")
    }
}