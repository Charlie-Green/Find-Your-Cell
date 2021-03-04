package by.sviazen.remote.retrofit.jail

import by.sviazen.domain.contract.jail.JailsListPojo
import by.sviazen.domain.contract.jail.SeatCountsPojo
import retrofit2.Call
import retrofit2.http.*


internal interface JailsService {

    @FormUrlEncoded
    @POST("jail/get")
    fun getJails(
        @Field("v") version: Int
    ): Call<JailsListPojo>

    @FormUrlEncoded
    @POST("jail/cell")
    fun getCells(
        @Field("v") version: Int,
        @Field("id") jailId: Int
    ): Call<SeatCountsPojo>
}