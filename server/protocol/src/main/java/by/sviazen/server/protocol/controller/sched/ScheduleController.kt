package by.sviazen.server.protocol.controller.sched

import by.sviazen.domain.contract.sched.ScheduleUpdatedPojo
import by.sviazen.domain.util.*
import by.sviazen.server.internal.repo.sched.ScheduleRepository
import by.sviazen.server.protocol.controller.shared.ControllerUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.InputStream


@RestController
class ScheduleController {

    @Autowired
    private lateinit var repo: ScheduleRepository

    @Autowired
    private lateinit var base64Coder: Base64Coder


    @PostMapping("sched/get")
    fun get(
        @RequestParam("v") version: Int,
        @RequestParam("id") arestId: Int,
        @RequestParam("pass") passwordBase64: String
    ): String {

        val pojo = ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64Coder.decode(passwordBase64)
            repo.get(arestId, passwordHash)
        }

        val approxSize = 64*(pojo.cells.size + pojo.periods.size)
        return Serializer.toJsonString(pojo, approxSize)
    }


    @PostMapping("sched/save")
    fun save(input: InputStream): String {
        
        ControllerUtil.catchingIllegalArgument {
            val pojo = Deserializer.fromJsonStream(input, ScheduleUpdatedPojo::class.java)
            val passwordHash = base64Coder.decode(pojo.passwordBase64)
            repo.save(pojo, passwordHash)
        }

        return ""
    }
}