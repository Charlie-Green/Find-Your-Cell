package by.zenkevich_churun.findcell.server.protocol.controller.jail

import by.zenkevich_churun.findcell.server.internal.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.server.protocol.serial.jail.abstr.JailsSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class JailsController {

    @Autowired
    private lateinit var repo: JailsRepository


    @PostMapping("jail/get")
    fun getJails(
        @RequestParam("v") version: Int
    ): String {

        val jails = repo.getJails()
        return serializer(version).serializeJails(jails)
    }


    @PostMapping("jail/cell")
    fun getCells(
        @RequestParam("v") version: Int,
        @RequestParam("id") jailId: Int
    ): String {

        val counts = repo
            .getSeatCounts(jailId)
            .toShortArray()
        return serializer(version).serializeCells(counts)
    }


    private fun serializer(version: Int): JailsSerializer {
        try {
            return JailsSerializer.forVersion(version)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }
    }
}