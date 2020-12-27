package by.zenkevich_churun.findcell.server.protocol.encode.auth

import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerSerializer
import by.zenkevich_churun.findcell.server.internal.repo.auth.LogInResponse
import by.zenkevich_churun.findcell.server.internal.repo.auth.SignUpResponse


internal class AuthorizationEncoder1: AuthorizationEncoder {

    private val serialer by lazy {
        PrisonerSerializer.forVersion(1)
            ?: throw Error("No ${PrisonerSerializer::class.java.simpleName} for version 1")
    }


    override fun encode(
        response: LogInResponse
    ): String = when(response) {

        is LogInResponse.Success       -> serialer.serialize(response.prisoner)
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