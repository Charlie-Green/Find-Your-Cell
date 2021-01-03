package by.zenkevich_churun.findcell.serial.sched.serial

import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import by.zenkevich_churun.findcell.serial.sched.v1.serial.ScheduleDeserializer1
import java.io.InputStream


interface ScheduleDeserializer {

    fun deserialize(input: InputStream): SchedulePojo


    companion object {

        fun forVersion(v: Int): ScheduleDeserializer {
            if(v == 1) {
                return ScheduleDeserializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}