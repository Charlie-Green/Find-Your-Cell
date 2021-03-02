package by.zenkevich_churun.findcell.server.protocol.controller.sync

import by.zenkevich_churun.findcell.domain.util.Base64Coder
import by.zenkevich_churun.findcell.domain.util.Serializer
import by.zenkevich_churun.findcell.server.internal.repo.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class SynchronizationController {

    @Autowired
    private lateinit var repo: SynchronizationRepository

    @Autowired
    private lateinit var base64: Base64Coder


    @PostMapping("sync")
    fun sync(
        @RequestParam("v") version: Int,
        @RequestParam("id") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String

    ): String = ControllerUtil.catchingIllegalArgument {
        val passwordHash = base64.decode(passwordBase64)
        val data = repo.synchronizedData(prisonerId, passwordHash)
        val approxSize = 64*data.coPrisoners.size + 396*data.jails.size
        Serializer.toJsonString(data, approxSize)
    }
}