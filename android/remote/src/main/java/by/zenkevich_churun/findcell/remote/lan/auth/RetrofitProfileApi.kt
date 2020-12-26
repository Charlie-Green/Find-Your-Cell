package by.zenkevich_churun.findcell.remote.lan.auth

import by.zenkevich_churun.findcell.contract.prisoner.decode.PrisonerDecoder
import by.zenkevich_churun.findcell.contract.prisoner.util.ProtocolUtil
import by.zenkevich_churun.findcell.core.api.auth.LogInResponse
import by.zenkevich_churun.findcell.core.api.auth.ProfileApi
import by.zenkevich_churun.findcell.core.api.auth.SignUpResponse
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.remote.lan.auth.entity.RetrofitPrisoner
import by.zenkevich_churun.findcell.remote.lan.common.RetrofitApisUtil
import by.zenkevich_churun.findcell.remote.lan.common.RetrofitHolder.retrofit
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

        val result = response.body()!!
        if(result[0] == 'U') {
            return LogInResponse.WrongUsername
        }
        if(result[0] == 'P') {
            return LogInResponse.WrongPassword
        }

        val resultStream = result.byteInputStream()
        val contractPrisoner = PrisonerDecoder.create().decode(resultStream)
        val retrofitPrisoner = RetrofitPrisoner.from(contractPrisoner)
        return LogInResponse.Success(retrofitPrisoner)
    }

    override fun signUp(username: String, name: String, passwordHash: ByteArray): SignUpResponse {
        TODO()
    }

    override fun update(prisoner: Prisoner, passwordHash: ByteArray) {
        TODO()
    }
}