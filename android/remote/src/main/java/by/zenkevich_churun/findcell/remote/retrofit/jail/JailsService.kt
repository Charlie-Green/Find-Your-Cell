package by.zenkevich_churun.findcell.remote.retrofit.jail

import by.zenkevich_churun.findcell.serial.jail.v1.pojo.JailsListPojo1
import retrofit2.Call
import retrofit2.http.*


internal interface JailsService {

    @FormUrlEncoded
    @POST("jail/get")
    fun getJails(
        @Field("v") version: Int
    ): Call<JailsListPojo1>
}