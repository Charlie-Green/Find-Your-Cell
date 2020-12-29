package by.zenkevich_churun.findcell.server.protocol.controller.arest

import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.server.protocol.di.ServerKoin
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.server.protocol.serial.arest.abstr.ArestsSerializer
import org.springframework.web.bind.annotation.*


@RestController
class ArestsController {

    private val repo by lazy {
        ServerKoin.instance().get(ArestsRepository::class)
    }


    @PostMapping("/arest/get")
    fun getArests(
        @RequestParam("v") version: Int,
        @RequestParam("id") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String
    ): String {

        val passwordHash = ProtocolUtil.decodeBase64(passwordBase64)

        val arests = try {
            repo.getArests(prisonerId, passwordHash)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }

        return ArestsSerializer
            .forVersion(version)
            .serialize(arests)
    }
}