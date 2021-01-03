package by.zenkevich_churun.findcell.server.protocol.controller.sched

import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleSerializer
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.sched.ScheduleRepository
import by.zenkevich_churun.findcell.server.protocol.controller.sched.map.ScheduleMapper
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


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

        val passwordHash = try {
            Base64Util.decode(passwordBase64)
        } catch(exc: IllegalArgumentException) {
            throw IllegalServerParameterException()
        }

        val view = repo.get(arestId, passwordHash)

        val pojo = try {
            ScheduleMapper.forVersion(version)
                .schedulePojo(view)
        } catch(exc: IllegalArgumentException) {
            println(exc.message)
            throw IllegalServerParameterException()
        }

        return ScheduleSerializer
            .forVersion(version)
            .serialize(pojo)
    }
}