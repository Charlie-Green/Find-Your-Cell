package by.zenkevich_churun.findcell.remote.retrofit.profile

import by.zenkevich_churun.findcell.domain.contract.auth.AuthorizedPrisonerPojo
import by.zenkevich_churun.findcell.domain.contract.auth.LogInResponse
import by.zenkevich_churun.findcell.domain.contract.auth.SignUpResponse
import by.zenkevich_churun.findcell.domain.util.Deserializer
import java.io.IOException
import java.io.InputStream


internal object ProfileDeserializer {

    fun logIn(
        istream: InputStream
    ): LogInResponse = when(val responseType = istream.read().toChar()) {
        'S' -> LogInResponse.Success( deserializePrisoner(istream) )
        'U' -> LogInResponse.WrongUsername
        'P' -> LogInResponse.WrongPassword
        else -> throw IOException(
            "Unknown ${LogInResponse::class.java.simpleName} $responseType" )
    }

    fun signUp(
        istream: InputStream,
        initialName: String
    ): SignUpResponse = when(val responseType = istream.read().toChar()) {
        'S' -> signUpSuccess(istream, initialName)
        'U' -> SignUpResponse.UsernameTaken
        else -> throw IOException(
        "Unknown ${SignUpResponse::class.java.simpleName} $responseType" )
    }


    private fun deserializePrisoner(
        istream: InputStream
    ) = Deserializer.fromJsonStream(istream, AuthorizedPrisonerPojo::class.java)

    private fun signUpSuccess(
        istream: InputStream,
        initialName: String
    ): SignUpResponse.Success {

        val pojo = AuthorizedPrisonerPojo()
        pojo.id = String(istream.readBytes(), Charsets.UTF_8).toInt()
        pojo.name = initialName

        return SignUpResponse.Success(pojo)
    }
}