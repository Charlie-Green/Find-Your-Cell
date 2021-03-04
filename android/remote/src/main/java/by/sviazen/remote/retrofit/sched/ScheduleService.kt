package by.sviazen.remote.retrofit.sched

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


internal interface ScheduleService {

    @FormUrlEncoded
    @POST("sched/get")
    fun get(
        @Field("v") version: Int,
        @Field("id") arestId: Int,
        @Field("pass") passwordBase64: String
    ): Call<ResponseBody>

    @POST("sched/save")
    fun save(
        @Body body: RequestBody
    ): Call<ResponseBody>


    @POST("cell/add")
    fun addCell(
        @Body body: RequestBody
    ): Call<ResponseBody>

    @POST("cell/update")
    fun updateCell(
        @Body body: RequestBody
    ): Call<ResponseBody>

        @POST("cell/delete")
    fun deleteCell(
        @Body body: RequestBody
    ): Call<ResponseBody>
}