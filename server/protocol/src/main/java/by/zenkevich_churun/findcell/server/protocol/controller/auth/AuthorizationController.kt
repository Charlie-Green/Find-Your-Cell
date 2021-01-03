package by.zenkevich_churun.findcell.server.protocol.controller.auth

import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerSerializer
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class AuthorizationController {

    @Autowired
    private lateinit var repo: AuthorizationRepository


    @PostMapping("/auth/login")
    fun logIn(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordHash: String
    ): String {

        AuthorizationValidator.validateCredentials(username, null)

        val serialer = PrisonerSerializer.forVersion(version)
        val response = repo.logIn(
            username,
            Base64Util.decode(passwordHash)
        )

        return serialer.serialize(response)
    }

    @PostMapping("/auth/signup")
    fun signUp(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordHash: String,
        @RequestParam("name") initialName: String
    ): String {

        AuthorizationValidator.validateCredentials(username, initialName)

        val serialer = PrisonerSerializer.forVersion(version)
        val response = repo.signUp(
            username,
            Base64Util.decode(passwordHash),
            initialName
        )

        return serialer.serialize(response)
    }
}