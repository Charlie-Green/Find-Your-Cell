package by.zenkevich_churun.findcell.server.protocol.controller.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.cp.CoPrisonersRepository
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.server.protocol.serial.cp.abstr.CoPrisonerSerializer
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

        val passwordHash = Base64Util.decode(passwordBase64)
        val cp = repo.coPrisoner(prisonerId, passwordHash, coPrisonerId)

        return CoPrisonerSerializer
            .forVersion(version)
            .serialize(cp.info, cp.contacts)
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
            val passwordHash = Base64Util.decode(passwordBase64)
            repositoryMethod(prisonerId, passwordHash, coPrisonerId)
        }

        return newRelation.ordinal.toString()
    }
}