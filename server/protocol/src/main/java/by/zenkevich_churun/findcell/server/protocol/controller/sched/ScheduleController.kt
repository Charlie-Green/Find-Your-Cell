package by.zenkevich_churun.findcell.server.protocol.controller.sched

import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.sched.ScheduleRepository
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

        val passwordHash = Base64Util.decode(passwordBase64)
        val periods = repo.get(arestId, passwordHash)

        val sb = StringBuilder("Totally ${periods.size} periods")
        for(p in periods) {
            sb.append("\n${p.key.start} - ${p.key.end}")
        }

        return sb.toString()
    }
}