package by.zenkevich_churun.findcell.server.protocol.controller.jail

import by.zenkevich_churun.findcell.server.internal.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.server.protocol.di.ServerKoin
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.server.protocol.serial.jail.abstr.JailsSerializer
import org.springframework.web.bind.annotation.*


@RestController
class JailsController {

    private val repo by lazy {
        ServerKoin.instance().get(JailsRepository::class)
    }


    @PostMapping("jail/get")
    fun getJails(
        @RequestParam("v") version: Int
    ): String {

        val jails = repo.getJails()

        val serializer = try {
            JailsSerializer.forVersion(version)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }

        return serializer.serialize(jails)
    }
}