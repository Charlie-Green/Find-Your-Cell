package by.zenkevich_churun.findcell.server.protocol.controller.arest

import by.zenkevich_churun.findcell.domain.contract.arest.*
import by.zenkevich_churun.findcell.domain.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.domain.util.*
import by.zenkevich_churun.findcell.server.internal.repo.arest.ArestsRepository
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

        val arest = Deserializer
            .fromJsonStream(istream, AddedArestPojo::class.java)

        val response = ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64.decode(arest.passwordBase64)
            repo.addArest(arest, passwordHash)
        }

        return when(response) {
            is CreateOrUpdateArestResponse.Success ->
                "S"

            is CreateOrUpdateArestResponse.ArestsIntersect ->
                "I${response.intersectedId}"

            is CreateOrUpdateArestResponse.NetworkError ->
                throw IllegalArgumentException("This is for client only")
        }
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

        val listPojo = ArestsListPojo.from(arests)
        val approxSize = 128*listPojo.arests.size
        return Serializer.toJsonString(listPojo, approxSize)
    }


    @PostMapping("/arest/delete")
    fun deleteArests(input: InputStream): String {
        val pojo = Deserializer.fromJsonStream(input, DeletedArestsPojo::class.java)

        ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64.decode(pojo.passwordBase64)
            repo.deleteArests(
                pojo.prisonerId,
                passwordHash,
                pojo.arestIds
            )
        }

        return ""
    }
}