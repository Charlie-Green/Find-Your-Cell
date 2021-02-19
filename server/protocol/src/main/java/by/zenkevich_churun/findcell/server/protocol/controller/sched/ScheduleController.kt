package by.zenkevich_churun.findcell.server.protocol.controller.sched

import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleDeserializer
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleSerializer
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.sched.ScheduleRepository
import by.zenkevich_churun.findcell.server.protocol.controller.sched.map.ScheduleMapper
import by.zenkevich_churun.findcell.server.protocol.controller.shared.ControllerUtil
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ScheduleController {

    @Autowired
    private lateinit var repo: ScheduleRepository


    @PostMapping("sched/get")
    fun get(
        @RequestParam("v") version: Int,
        @RequestParam("id") arestId: Int,
        @RequestParam("pass") passwordBase64: String
    ): String {

        val pojo = ControllerUtil.catchingIllegalArgument {
            val passwordHash = Base64Util.decode(passwordBase64)
            val view = repo.get(arestId, passwordHash)
            ScheduleMapper.forVersion(version).schedulePojo(view)
        }

        return ScheduleSerializer
            .forVersion(version)
            .serialize(pojo)
    }


    @PostMapping("sched/save")
    fun save(input: InputStream): String {
        val pojo = ScheduleDeserializer
            .forVersion(1)
            .deserializeLight(input)

        val arestId = pojo.arestId
            ?: throw IllegalServerParameterException("Arest ID must be provided")
        val passwordBase64 = pojo.passwordBase64
            ?: throw IllegalServerParameterException("Password must be provided")
        val passwordHash = Base64Util.decode(passwordBase64)

        repo.save(arestId, pojo.periods, passwordHash)

        return ""
    }
}