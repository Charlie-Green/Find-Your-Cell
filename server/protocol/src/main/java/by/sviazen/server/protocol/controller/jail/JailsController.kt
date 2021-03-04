package by.sviazen.server.protocol.controller.jail

import by.sviazen.domain.util.Serializer
import by.sviazen.server.internal.repo.jail.JailsRepository
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

        val pojo = repo.getJails()
        val approxSize = 64*pojo.jails.size
        return Serializer.toJsonString(pojo, approxSize)
    }


    @PostMapping("jail/cell")
    fun getCells(
        @RequestParam("v") version: Int,
        @RequestParam("id") jailId: Int
    ): String {

        val counts = repo.getSeatCounts(jailId)
        val approxSize = 4*counts.seatCounts.size + 16
        return Serializer.toJsonString(counts, approxSize)
    }
}