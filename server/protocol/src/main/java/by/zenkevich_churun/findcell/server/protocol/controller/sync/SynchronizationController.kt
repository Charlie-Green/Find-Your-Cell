package by.zenkevich_churun.findcell.server.protocol.controller.sync

import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.sync.SynchronizationRepository
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.server.protocol.serial.sync.abstr.SynchronizationSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class SynchronizationController {

    @Autowired
    private lateinit var repo: SynchronizationRepository


    @PostMapping("sync")
    fun sync(
        @RequestParam("v") version: Int,
        @RequestParam("id") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String
    ): String {

        val passwordHash = try {
            Base64Util.decode(passwordBase64)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }

        val data = repo.synchronizedData(prisonerId, passwordHash)

        val serialer = try {
            SynchronizationSerializer.forVersion(version)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }

        return serialer.serialize(data)
    }
}