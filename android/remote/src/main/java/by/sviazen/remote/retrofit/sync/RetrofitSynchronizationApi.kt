package by.sviazen.remote.retrofit.sync

import by.sviazen.core.api.sync.SynchronizationApi
import by.sviazen.domain.contract.sync.SynchronizedPojo
import by.sviazen.domain.util.Base64Coder
import by.sviazen.remote.retrofit.common.RetrofitHolder
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitSynchronizationApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder,
    private val base64: Base64Coder
): SynchronizationApi {

    override fun fetchData(
        prisonerId: Int,
        passwordHash: ByteArray
    ): SynchronizedPojo {

        val passBase64 = base64.encode(passwordHash)

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