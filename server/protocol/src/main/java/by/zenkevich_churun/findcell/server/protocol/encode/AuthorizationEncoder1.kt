package by.zenkevich_churun.findcell.server.protocol.encode

import by.zenkevich_churun.findcell.protocol.prisoner.encode.PrisonerEncoder
import by.zenkevich_churun.findcell.server.internal.repo.auth.LogInResponse
import by.zenkevich_churun.findcell.server.internal.repo.auth.SignUpResponse


internal class AuthorizationEncoder1: AuthorizationEncoder {

    private val encoder by lazy {
        PrisonerEncoder.forVersion(1)
            ?: throw Error("No ${PrisonerEncoder::class.java.simpleName} for version 1")
    }


    override fun encode(
        response: LogInResponse
    ): String = when(response) {

        is LogInResponse.Success       -> encoder.encode(response.prisoner)
        is LogInResponse.WrongUsername -> "U"
        is LogInResponse.WrongPassword -> "P"
    }

    override fun encode(
        response: SignUpResponse
    ): String = when(response) {

        is SignUpResponse.Success       -> "${response.prisonerId}"
        is SignUpResponse.UsernameTaken -> "U"
    }
}