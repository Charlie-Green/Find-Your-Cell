package by.zenkevich_churun.findcell.remote.retrofit.arest

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


internal interface ArestsService {

    @FormUrlEncoded
    @POST("arest/get")
    fun getArests(
        @Field("v") version: Int,
        @Field("id") prisonerId: Int,
        @Field("pass") passwordBase64: String
    ): Call<ResponseBody>

    @POST("arest/add")
    fun createArest(
        @Body data: RequestBody
    ): Call<ResponseBody>
}