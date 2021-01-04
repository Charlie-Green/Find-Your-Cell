package by.zenkevich_churun.findcell.remote.retrofit.jail

import by.zenkevich_churun.findcell.serial.jail.v1.pojo.JailsListPojo1
import by.zenkevich_churun.findcell.serial.jail.v1.pojo.SeatCountsListPojo
import retrofit2.Call
import retrofit2.http.*


internal interface JailsService {

    @FormUrlEncoded
    @POST("jail/get")
    fun getJails(
        @Field("v") version: Int
    ): Call<JailsListPojo1>

    @FormUrlEncoded
    @POST("jail/cell")
    fun getCells(
        @Field("v") version: Int,
        @Field("id") jailId: Int
    ): Call<SeatCountsListPojo>
}