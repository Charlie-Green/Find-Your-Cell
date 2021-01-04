package by.zenkevich_churun.findcell.serial.sched.v1.serial

import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleDeserializer
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.SchedulePojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import java.io.InputStream


class ScheduleDeserializer1: ScheduleDeserializer {

    override fun deserialize(input: InputStream): SchedulePojo {
        // TODO: CharlieDebug:
        val json = String(input.readBytes())
        println(json)
        return ProtocolUtil.fromJson(json.byteInputStream(), SchedulePojo1::class.java)
    }
}