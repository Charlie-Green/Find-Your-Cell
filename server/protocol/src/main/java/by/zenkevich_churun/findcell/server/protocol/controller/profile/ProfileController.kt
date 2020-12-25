package by.zenkevich_churun.findcell.server.protocol.controller.profile

import by.zenkevich_churun.findcell.protocol.prisoner.decode.PrisonerDecoder
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ProfileController {

    @PostMapping("/profile/update")
    fun update(
        istream: InputStream
    ): String {

        val decoder = PrisonerDecoder.create()
        val prisoner = decoder.decode(istream)

        // TODO: CharlieDebug
        return "name=\"${prisoner.name}\", passwordHash=${String(prisoner.passwordHash ?: byteArrayOf())}"
    }
}