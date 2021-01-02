package by.zenkevich_churun.findcell.remote.retrofit.arest

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


internal interface ArestsService {

    @FormUrlEncoded
    @POST("arest/get")
    fun get(
        @Field("v") version: Int,
        @Field("id") prisonerId: Int,
        @Field("pass") passwordBase64: String
    ): Call<ResponseBody>

    @POST("arest/add")
    fun create(
        @Body data: RequestBody
    ): Call<ResponseBody>

    @POST("arest/delete")
    fun delete(
        @Body data: RequestBody
    ): Call<ResponseBody>
}