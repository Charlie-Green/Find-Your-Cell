package by.sviazen.server.protocol.controller.cp

import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.domain.util.Base64Coder
import by.sviazen.domain.util.Serializer
import by.sviazen.server.internal.repo.cp.CoPrisonersRepository
import by.sviazen.server.protocol.controller.shared.ControllerUtil
import by.sviazen.server.protocol.exc.NotConnectedCoPrisonersException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class CoPrisonersController {

    @Autowired
    private lateinit var repo: CoPrisonersRepository

    @Autowired
    private lateinit var base64Coder: Base64Coder


    @PostMapping("cp/add")
    fun connect(
        @RequestParam("v") version: Int,
        @RequestParam("id1") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("id2") coPrisonerId: Int

    ): String = changeRelation(
        prisonerId,
        passwordBase64,
        coPrisonerId,
        repo::connect
    )


    @PostMapping("cp/remove")
    fun disconnect(
        @RequestParam("v") version: Int,
        @RequestParam("id1") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("id2") coPrisonerId: Int

    ): String = changeRelation(
        prisonerId,
        passwordBase64,
        coPrisonerId,
        repo::disconnect
    )


    @PostMapping("cp/get")
    fun coPrisoner(
        @RequestParam("v") version: Int,
        @RequestParam("id1") prisonerId: Int,
        @RequestParam("pass") passwordBase64: String,
        @RequestParam("id2") coPrisonerId: Int
    ): String {

        val passwordHash = base64Coder.decode(passwordBase64)
        val cp = repo
            .coPrisoner(prisonerId, passwordHash, coPrisonerId)
            ?: throw NotConnectedCoPrisonersException(prisonerId, coPrisonerId)

        return Serializer.toJsonString(cp, 128)
    }


    private inline fun changeRelation(
        prisonerId: Int,
        passwordBase64: String,
        coPrisonerId: Int,
        repositoryMethod: (
            prisonerId: Int,
            passwordHash: ByteArray,
            coPrisonerId: Int
        ) -> CoPrisoner.Relation
    ): String {

        val newRelation = ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64Coder.decode(passwordBase64)
            repositoryMethod(prisonerId, passwordHash, coPrisonerId)
        }

        return newRelation.ordinal.toString()
    }
}