package by.zenkevich_churun.findcell.remote.lan.profile

import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.core.api.auth.LogInResponse
import by.zenkevich_churun.findcell.core.api.auth.ProfileApi
import by.zenkevich_churun.findcell.core.api.auth.SignUpResponse
import by.zenkevich_churun.findcell.entity.Prisoner
import by.zenkevich_churun.findcell.remote.lan.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.lan.common.RetrofitHolder.retrofit
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerDeserializer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitProfileApi @Inject constructor(): ProfileApi {

    override fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val passwordBase64 = ProtocolUtil.encodeBase64(passwordHash)

        val service = retrofit.create(ProfileService::class.java)
        val response = service.login(1, username, passwordBase64).execute()
        RetrofitApisUtil.assertResponseCode(response.code())

        val result = response.body()!!.string()
        if(result[0] == 'U') {
            return LogInResponse.WrongUsername
        }
        if(result[0] == 'P') {
            return LogInResponse.WrongPassword
        }

        val resultStream = result.byteInputStream()
        val prisoner = PrisonerDeserializer
            .forVersion(1)
            .deserialize(resultStream)
        return LogInResponse.Success(prisoner)
    }

    override fun signUp(username: String, name: String, passwordHash: ByteArray): SignUpResponse {
        TODO()
    }

    override fun update(prisoner: Prisoner, passwordHash: ByteArray) {
        TODO()
    }
}