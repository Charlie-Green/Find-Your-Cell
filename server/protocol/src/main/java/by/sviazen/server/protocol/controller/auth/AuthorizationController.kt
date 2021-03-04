package by.sviazen.server.protocol.controller.auth

import by.sviazen.domain.util.Base64Coder
import by.sviazen.server.internal.repo.auth.AuthorizationRepository
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

        val response = repo.logIn(
            username,
            base64.decode(passwordBase64)
        )

        return AuthorizationSerializer.serialize(response)
    }

    @PostMapping("/auth/signup")
    fun signUp(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("name") initialName: String
    ): String {

        AuthorizationValidator.validateCredentials(username, initialName)

        val response = repo.signUp(
            username,
            base64.decode(passwordBase64),
            initialName
        )

        return AuthorizationSerializer.serialize(response)
    }
}