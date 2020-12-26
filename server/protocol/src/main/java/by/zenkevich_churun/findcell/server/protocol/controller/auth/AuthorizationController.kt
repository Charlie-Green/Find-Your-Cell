package by.zenkevich_churun.findcell.server.protocol.controller.auth

import by.zenkevich_churun.findcell.contract.prisoner.util.ProtocolUtil
import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
import by.zenkevich_churun.findcell.server.protocol.di.ServerKoin
import by.zenkevich_churun.findcell.server.protocol.encode.auth.AuthorizationEncoder
import org.springframework.web.bind.annotation.*


@RestController
class AuthorizationController {

    private val repo by lazy {
        ServerKoin.instance().get(AuthorizationRepository::class)
    }


    @PostMapping("/auth/login")
    fun logIn(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordHash: String
    ): String {

        AuthorizationValidator.validateCredentials(username, null)

        val encoder = AuthorizationEncoder.forVersion(version)
        val response = repo.logIn(
            username,
            ProtocolUtil.decodeBase64(passwordHash)
        )
        println("Response is ${response.javaClass.simpleName}")

        return encoder.encode(response)
    }

    @PostMapping("/auth/signup")
    fun signUp(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordHash: String,
        @RequestParam("name") initialName: String
    ): String {

        AuthorizationValidator.validateCredentials(username, initialName)

        val encoder = AuthorizationEncoder.forVersion(version)
        val response = repo.signUp(
            username,
            ProtocolUtil.decodeBase64(passwordHash),
            initialName
        )

        return encoder.encode(response)
    }
}