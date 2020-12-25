package by.zenkevich_churun.findcell.server.protocol.encode

import by.zenkevich_churun.findcell.server.internal.repo.auth.LogInResponse
import by.zenkevich_churun.findcell.server.internal.repo.auth.SignUpResponse
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException


interface AuthorizationEncoder {
    fun encode(response: LogInResponse): String
    fun encode(response: SignUpResponse): String


    companion object {

        fun forVersion(v: Int): AuthorizationEncoder = when(v) {
            1  -> AuthorizationEncoder1()

            else -> {
                println("Unknown version $v")
                throw IllegalServerParameterException()
            }
        }
    }
}