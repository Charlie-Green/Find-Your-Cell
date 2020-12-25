package by.zenkevich_churun.findcell.server.protocol.controller.profile

import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ProfileController {

    @PostMapping("/profile/update")
    fun update(
        @RequestBody istream: InputStream
    ): String {

        TODO()
    }
}