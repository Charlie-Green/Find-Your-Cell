package by.zenkevich_churun.findcell.remote.retrofit.profile

import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.core.api.auth.*
import by.zenkevich_churun.findcell.entity.Prisoner
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.retrofit.common.RetrofitHolder
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerDeserializer
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerSerializer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitProfileApi @Inject constructor(
    private val retrofitHolder: RetrofitHolder
): ProfileApi {

    override fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val passwordBase64 = ProtocolUtil.encodeBase64(passwordHash)

        val service = retrofit.create(ProfileService::class.java)
        val response = service.logIn(1, username, passwordBase64).execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val result = response.body()!!.string()
        if(result == "U") {
            return LogInResponse.WrongUsername
        }
        if(result == "P") {
            return LogInResponse.WrongPassword
        }

        val resultStream = result.byteInputStream()
        val prisoner = PrisonerDeserializer
            .forVersion(1)
            .deserialize(resultStream)
        return LogInResponse.Success(prisoner)
    }

    override fun signUp(
        username: String,
        name: String,
        passwordHash: ByteArray
    ): SignUpResponse {

        val passwordBase64 = ProtocolUtil.encodeBase64(passwordHash)

        val service = retrofit.create(ProfileService::class.java)
        val response = service
            .signUp(1, username, passwordBase64, name)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val result = response.body()!!.string()
        if(result == "U") {
            return SignUpResponse.UsernameExists
        }

        val id = result.toInt()
        val prisoner = SignedUpPrisoner(id, username, passwordHash, name)
        return SignUpResponse.Success(prisoner)
    }

    override fun update(prisoner: Prisoner) {
        val serialized = PrisonerSerializer
            .forVersion(1)
            .serialize(prisoner)

        val service = retrofit.create(ProfileService::class.java)
        val response = service
            .update(serialized)
            .execute()
        RetrofitApisUtil.assertResponseCode(response.code())
    }


    private val retrofit
        get() = retrofitHolder.retrofit
}