package by.sviazen.server.protocol.controller.profile

import by.sviazen.domain.contract.prisoner.UpdatedPrisonerPojo
import by.sviazen.domain.util.Base64Coder
import by.sviazen.domain.util.Deserializer
import by.sviazen.server.internal.repo.profile.ProfileRepository
import by.sviazen.server.protocol.controller.shared.ControllerUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ProfileController {

    @Autowired
    private lateinit var repo: ProfileRepository

    @Autowired
    private lateinit var base64: Base64Coder


    @PostMapping("/profile/update")
    fun update(
        istream: InputStream
    ): String {

        val prisoner = Deserializer
            .fromJsonStream(istream, UpdatedPrisonerPojo::class.java)

        ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64.decode(prisoner.passwordBase64)
            repo.update(prisoner, passwordHash)
        }

        return ""
    }
}