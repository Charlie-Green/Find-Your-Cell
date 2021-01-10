package by.zenkevich_churun.findcell.remote.retrofit.cp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


internal interface CoPrisonersService {

    @FormUrlEncoded
    @POST("cp/add")
    fun connect(
        @Field("v") version: Int,
        @Field("id1") prisonerId: Int,
        @Field("pass") passwordHash: ByteArray,
        @Field("id2") coPrisonerId: Int
    ): Call<ResponseBody>

}