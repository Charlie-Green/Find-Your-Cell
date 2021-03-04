package by.sviazen.remote.retrofit.sync

import by.sviazen.domain.contract.sync.SynchronizedPojo
import retrofit2.Call
import retrofit2.http.*


internal interface SynchronizationService {

    @FormUrlEncoded
    @POST("sync")
    fun fetchData(
        @Field("v") version: Int,
        @Field("id") prisonerId: Int,
        @Field("pass") passwordBase64: String
    ): Call<SynchronizedPojo>
}