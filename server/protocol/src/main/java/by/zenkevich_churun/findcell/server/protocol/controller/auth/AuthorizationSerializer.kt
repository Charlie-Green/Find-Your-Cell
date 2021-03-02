package by.zenkevich_churun.findcell.server.protocol.controller.auth

import by.zenkevich_churun.findcell.domain.contract.auth.LogInResponse
import by.zenkevich_churun.findcell.domain.contract.auth.SignUpResponse
import by.zenkevich_churun.findcell.domain.util.Serializer


internal object AuthorizationSerializer {

    fun serialize(
        response: LogInResponse
    ): String = when(response) {
        is LogInResponse.Success ->
            "S${Serializer.toJsonString(response.prisoner)}"

        is LogInResponse.WrongUsername ->
            "U"

        is LogInResponse.WrongPassword ->
            "P"

        is LogInResponse.NetworkError ->
            throw IllegalArgumentException("NetworkError response is for client only")
    }


    fun serialize(
        response: SignUpResponse
    ): String = when(response) {
        is SignUpResponse.Success ->
            "S${Serializer.toJsonString(response.prisoner)}"

        is SignUpResponse.UsernameTaken ->
            "U"

        is SignUpResponse.NetworkError ->
            throw IllegalArgumentException("NetworkError response is for client only")
    }
}