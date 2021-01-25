package by.zenkevich_churun.findcell.server.protocol.controller.profile

import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerDeserializer
import by.zenkevich_churun.findcell.server.internal.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ProfileController {

    @Autowired
    private lateinit var repo: ProfileRepository


    @PostMapping("/profile/update")
    fun update(
        istream: InputStream
    ): String {

        val prisoner = PrisonerDeserializer
            .forVersion(1)
            .deserializePrisoner(istream)

        ControllerUtil.catchingIllegalArgument {
            repo.update(prisoner)
        }

        return ""
    }
}