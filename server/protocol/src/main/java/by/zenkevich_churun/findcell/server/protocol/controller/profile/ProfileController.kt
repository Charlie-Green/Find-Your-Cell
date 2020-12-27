package by.zenkevich_churun.findcell.server.protocol.controller.profile

import by.zenkevich_churun.findcell.serial.prisoner.common.PrisonerDeserializer
import by.zenkevich_churun.findcell.server.internal.repo.profile.ProfileRepository
import by.zenkevich_churun.findcell.server.protocol.di.ServerKoin
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ProfileController {

    private val repo by lazy {
        ServerKoin.instance().get(ProfileRepository::class)
    }


    @PostMapping("/profile/update")
    fun update(
        istream: InputStream
    ): String {

        val prisoner = PrisonerDeserializer
            .forVersion(1)
            .deserialize(istream)

        try {
            repo.update(prisoner)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }

        return ""
    }
}