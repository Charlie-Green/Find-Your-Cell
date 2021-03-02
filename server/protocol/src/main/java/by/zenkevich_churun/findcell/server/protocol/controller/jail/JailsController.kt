package by.zenkevich_churun.findcell.server.protocol.controller.jail

import by.zenkevich_churun.findcell.domain.util.Serializer
import by.zenkevich_churun.findcell.server.internal.repo.jail.JailsRepository
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
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
        return Serializer.toJsonString(jails)
    }


    @PostMapping("jail/cell")
    fun getCells(
        @RequestParam("v") version: Int,
        @RequestParam("id") jailId: Int
    ): String {

        val counts = repo.getSeatCounts(jailId)
        return Serializer.toJsonString(counts)
    }
}