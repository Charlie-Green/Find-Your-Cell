package by.zenkevich_churun.findcell.server.protocol.controller.arest

import by.zenkevich_churun.findcell.contract.prisoner.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.server.protocol.di.ServerKoin
import by.zenkevich_churun.findcell.server.protocol.encode.arest.ArestsEncoder
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
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

        try {
            val arests = repo.getArests(prisonerId, passwordHash)
            val encoder = ArestsEncoder.forVersion(version)
            return encoder.encode(arests)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }
    }
}