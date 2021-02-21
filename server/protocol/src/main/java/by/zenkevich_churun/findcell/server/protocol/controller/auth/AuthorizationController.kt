package by.zenkevich_churun.findcell.server.protocol.controller.auth

import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerSerializer
import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class AuthorizationController {

    @Autowired
    private lateinit var repo: AuthorizationRepository

    @Autowired
    private lateinit var base64: Base64Coder


    @PostMapping("/auth/login")
    fun logIn(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordBase64: String
    ): String {

        AuthorizationValidator.validateCredentials(username, null)

        val serialer = PrisonerSerializer.forVersion(base64, version)
        val response = repo.logIn(
            username,
            base64.decode(passwordBase64)
        )

        return serialer.serialize(response)
    }

    @PostMapping("/auth/signup")
    fun signUp(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("name") initialName: String
    ): String {

        AuthorizationValidator.validateCredentials(username, initialName)

        val serialer = PrisonerSerializer.forVersion(base64, version)
        val response = repo.signUp(
            username,
            base64.decode(passwordBase64),
            initialName
        )

        return serialer.serialize(response)
    }
}