package by.sviazen.remote.retrofit.profile

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


internal interface ProfileService {

    @FormUrlEncoded
    @POST("auth/login")
    fun logIn(
        @Field("v",     encoded = true) version: Int,
        @Field("uname", encoded = true) username: String,
        @Field("pass",  encoded = true) passwordBase64: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("auth/signup")
    fun signUp(
        @Field("v")     version: Int,
        @Field("uname") username: String,
        @Field("pass")  passwordBase64: String,
        @Field("name")  initialName: String
    ): Call<ResponseBody>

    @POST("profile/update")
    fun update(
        @Body prisoner: RequestBody
    ): Call<ResponseBody>
}