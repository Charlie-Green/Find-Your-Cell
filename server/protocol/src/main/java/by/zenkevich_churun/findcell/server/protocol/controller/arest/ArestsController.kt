package by.zenkevich_churun.findcell.server.protocol.controller.arest

import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.serial.arest.serial.*
import by.zenkevich_churun.findcell.serial.common.abstr.Base64Coder
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ArestsController {

    @Autowired
    private lateinit var repo: ArestsRepository

    @Autowired
    private lateinit var base64: Base64Coder


    @PostMapping("/arest/add")
    fun addArest(istream: InputStream): String {

        val arest = ArestsDeserializer
            .forVersion(1)
            .deserializeOne(istream)

        val prisonerId = arest.prisonerId
        val passwordBase64 = arest.passwordBase64
        if(prisonerId == null || passwordBase64 == null) {
            throw IllegalServerParameterException("Add Arest: credentials not specified")
        }

        val response = ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64.decode(passwordBase64)
            repo.addArest(arest, prisonerId, passwordHash)
        }

        return ArestsSerializer
            .forVersion(1, base64)
            .sertialize(response)
    }


    @PostMapping("/arest/get")
    fun getArests(
        @RequestParam("v") version: Int,
        @RequestParam("id") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String
    ): String {

        val arests = ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64.decode(passwordBase64)
            repo.getArests(prisonerId, passwordHash)
        }

        return ArestsSerializer
            .forVersion(version, base64)
            .serialize(arests)
    }


    @PostMapping("/arest/delete")
    fun deleteArests(input: InputStream): String {
        val pojo = ArestsDeserializer
            .forVersion(1)
            .deserializeIds(input)

        ControllerUtil.catchingIllegalArgument {
            repo.deleteArests(
                pojo.prisonerId,
                pojo.passwordHash,
                pojo.arestIds
            )
        }

        return ""
    }
}