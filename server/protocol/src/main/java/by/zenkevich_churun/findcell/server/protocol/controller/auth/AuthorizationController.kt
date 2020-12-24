package by.zenkevich_churun.findcell.server.protocol.controller.auth

import by.zenkevich_churun.findcell.server.internal.repo.auth.AuthorizationRepository
import by.zenkevich_churun.findcell.server.protocol.util.ControllerUtil
import org.springframework.web.bind.annotation.*


@RestController
class AuthorizationController {

    // TODO: DI this:
    private val repo = AuthorizationRepository()


    @PostMapping("/auth/login")
    fun logIn(
        @RequestParam("v") version: Int,
        @RequestParam("uname") username: String,
        @RequestParam("pass") passwordHash: String
    ): String {

        println("Version = $version")
        val response = repo.logIn(
            username.also { println("username=\"$it\"") },
            ControllerUtil.decodeBase64(passwordHash).also { println("password=\"${String(it)}\"") }
        )

        return AuthorizationMapping.encode(response)
    }
}