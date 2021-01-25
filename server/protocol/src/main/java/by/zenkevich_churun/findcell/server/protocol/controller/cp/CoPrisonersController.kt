package by.zenkevich_churun.findcell.server.protocol.controller.cp

import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class CoPrisonersController {

    @Autowired
    private lateinit var repo: CoPrisonersRepository


    @PostMapping("cp/add")
    fun connect(
        @RequestParam("v") version: Int,
        @RequestParam("id1") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("id2") coPrisonerId: Int
    ): String {

        val relation = ControllerUtil.catchingIllegalArgument {
            val passwordHash = Base64Util.decode(passwordBase64)

            repo.connect(
                prisonerId,
                passwordHash,
                coPrisonerId
            )
        }

        return relation.ordinal.toString()
    }


    @PostMapping("cp/remove")
    fun disconnect(
        @RequestParam("v") version: Int,
        @RequestParam("id1") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("id2") coPrisonerId: Int
    ): String {

        TODO()
    }
}