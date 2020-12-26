package by.zenkevich_churun.findcell.server.protocol.controller.arest

import by.zenkevich_churun.findcell.contract.prisoner.util.ProtocolUtil
import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.server.protocol.di.ServerKoin
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
        TODO()
    }
}