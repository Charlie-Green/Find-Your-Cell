package by.zenkevich_churun.findcell.server.protocol.controller.cp

import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.cp.CoPrisonersRepository
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


        try {
            val passwordHash = Base64Util.decode(passwordBase64)

            val response = repo.connect(
                prisonerId,
                passwordHash,
                coPrisonerId
            )
            return response.ordinal.toString()

        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }
    }
}