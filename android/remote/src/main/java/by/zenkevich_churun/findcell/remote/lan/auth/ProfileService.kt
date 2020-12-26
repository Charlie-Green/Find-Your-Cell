package by.zenkevich_churun.findcell.remote.lan.auth

import by.zenkevich_churun.findcell.remote.lan.auth.pojo.PrisonerPojo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


internal interface ProfileService {

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("v") version: Int,
        @Field("uname") username: String,
        @Field("pass") passwordBase64: String
    ): Call<PrisonerPojo>
}