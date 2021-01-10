package by.zenkevich_churun.findcell.remote.retrofit.sync

import by.zenkevich_churun.findcell.serial.sync.v1.SynchronizedPojo1
import retrofit2.Call
import retrofit2.http.*


internal interface SynchronizationService {

    @FormUrlEncoded
    @POST("sync")
    fun fetchData(
        @Field("v") version: Int,
        @Field("id") prisonerId: Int,
        @Field("pass") passwordBase64: String
    ): Call<SynchronizedPojo1>
}