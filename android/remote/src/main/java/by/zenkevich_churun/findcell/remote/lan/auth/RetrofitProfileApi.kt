package by.zenkevich_churun.findcell.remote.lan.auth

import by.zenkevich_churun.findcell.core.api.auth.LogInResponse
import by.zenkevich_churun.findcell.core.api.auth.ProfileApi
import by.zenkevich_churun.findcell.core.api.auth.SignUpResponse
import by.zenkevich_churun.findcell.core.entity.general.Prisoner
import by.zenkevich_churun.findcell.remote.lan.common.RetrofitHolder.retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitProfileApi @Inject constructor(): ProfileApi {

    override fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val service = retrofit.create(ProfileService::class.java)
        TODO()
    }

    override fun signUp(username: String, name: String, passwordHash: ByteArray): SignUpResponse {
        TODO()
    }

    override fun update(prisoner: Prisoner, passwordHash: ByteArray) {
        TODO()
    }
}