package by.zenkevich_churun.findcell.serial.sched.serial

import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import by.zenkevich_churun.findcell.serial.sched.v1.serial.ScheduleSerializer1


interface ScheduleSerializer {

    fun serialize(schedule: SchedulePojo): String


    companion object {

        fun forVersion(v: Int): ScheduleSerializer {
            if(v == 1) {
                return ScheduleSerializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}