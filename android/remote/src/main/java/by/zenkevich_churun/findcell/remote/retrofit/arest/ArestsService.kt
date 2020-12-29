package by.zenkevich_churun.findcell.remote.retrofit.arest

import by.zenkevich_churun.findcell.serial.arest.v1.pojo.ArestsListPojo1
import retrofit2.Call
import retrofit2.http.*


internal interface ArestsService {

    @FormUrlEncoded
    @POST("arest/get")
    fun getArests(
        @Field("v") version: Int,
        @Field("id") prisonerId: Int,
        @Field("pass") passwordBase64: String
    ): Call<ArestsListPojo1>
}