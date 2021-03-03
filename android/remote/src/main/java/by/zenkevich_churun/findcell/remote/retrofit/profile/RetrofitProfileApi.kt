package by.zenkevich_churun.findcell.remote.retrofit.profile

import by.zenkevich_churun.findcell.core.api.auth.*
import by.zenkevich_churun.findcell.domain.contract.auth.AuthorizedPrisonerPojo
import by.zenkevich_churun.findcell.domain.contract.auth.LogInResponse
import by.zenkevich_churun.findcell.domain.contract.auth.SignUpResponse
import by.zenkevich_churun.findcell.domain.contract.prisoner.UpdatedPrisonerPojo
import by.zenkevich_churun.findcell.domain.entity.Prisoner
import by.zenkevich_churun.findcell.domain.util.*
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitProfileApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder,
    private val base64: Base64Coder
): ProfileApi {

    override fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val passwordBase64 = base64.encode(passwordHash)

        val service = retrofit.create(ProfileService::class.java)
        val response = service.logIn(1, username, passwordBase64).execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val istream = response.body()!!.byteStream()
        return ProfileDeserializer.logIn(istream)
    }

    override fun signUp(
        username: String,
        name: String,
        passwordHash: ByteArray
    ): SignUpResponse {

        val passwordBase64 = base64.encode(passwordHash)

        val service = retrofit.create(ProfileService::class.java)
        val response = service
            .signUp(1, username, passwordBase64, name)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val istream = response.body()!!.byteStream()
        return ProfileDeserializer.signUp(istream, name)
    }

    override fun update(prisoner: Prisoner) {
        val passwordHash = prisoner.passwordHash
            ?: throw IllegalArgumentException("'Update Profile' requires password hash")
        val passwordBase64 = base64.encode(passwordHash)
        val pojo = UpdatedPrisonerPojo.from(prisoner, passwordBase64)
        val approxSize = pojo.passwordBase64.length + 24*prisoner.contacts.size + 72
        val serialized = Serializer.toJsonString(pojo, approxSize)

        val bodyType = MediaType.get("text/plain")
        val service = retrofit.create(ProfileService::class.java)
        val response = service
            .update( RequestBody.create(bodyType, serialized) )
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}