package by.zenkevich_churun.findcell.server.protocol.controller.sync

import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.server.internal.repo.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import by.zenkevich_churun.findcell.server.protocol.serial.sync.abstr.SynchronizationSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class SynchronizationController {

    @Autowired
    private lateinit var repo: SynchronizationRepository

    @Autowired
    private lateinit var base64Coder: Base64Coder


    @PostMapping("sync")
    fun sync(
        @RequestParam("v") version: Int,
        @RequestParam("id") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String

    ): String = ControllerUtil.catchingIllegalArgument {
        val passwordHash = base64Coder.decode(passwordBase64)
        val data = repo.synchronizedData(prisonerId, passwordHash)
        val serialer = SynchronizationSerializer.forVersion(version)
        return serialer.serialize(data)
    }
}