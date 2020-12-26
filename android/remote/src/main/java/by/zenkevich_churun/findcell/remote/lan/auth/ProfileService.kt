package by.zenkevich_churun.findcell.remote.lan.auth

import retrofit2.Call
import retrofit2.http.*


internal interface ProfileService {

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("v") version: Int,
        @Field("uname") username: String,
        @Field("pass") passwordBase64: String
    ): Call<String>
}