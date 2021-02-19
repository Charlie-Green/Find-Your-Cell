package by.zenkevich_churun.findcell.serial.sched.v1.serial

import by.zenkevich_churun.findcell.serial.sched.pojo.LightSchedulePojo
import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleSerializer
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.LightSchedulePojo1
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.SchedulePojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil


class ScheduleSerializer1: ScheduleSerializer {

    override fun serialize(schedule: SchedulePojo): String {
        if(schedule !is SchedulePojo1) {
            ProtocolUtil.throwWrongVersion(true, 1)
        }

        val approxSize = 54 + 72*schedule.cells.size + 54*schedule.periods.size
        return ProtocolUtil.toJson(schedule, approxSize)
    }

    override fun serializeLight(schedule: LightSchedulePojo): String {
        if(schedule !is LightSchedulePojo1) {
            ProtocolUtil.throwWrongVersion(true, 1)
        }

        val approxSize = 54*(schedule.periods.size + 1)
        return ProtocolUtil.toJson(schedule, approxSize)
    }
}