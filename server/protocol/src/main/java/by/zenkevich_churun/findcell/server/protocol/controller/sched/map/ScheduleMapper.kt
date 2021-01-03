package by.zenkevich_churun.findcell.server.protocol.controller.sched.map

import by.zenkevich_churun.findcell.serial.sched.pojo.SchedulePojo
import by.zenkevich_churun.findcell.serial.sched.serial.ScheduleSerializer
import by.zenkevich_churun.findcell.server.internal.entity.view.ScheduleView
import by.zenkevich_churun.findcell.server.internal.repo.sched.ScheduleRepository


/** This class appears because there is no common abstract class
  * that [ScheduleRepository] could return and [ScheduleSerializer] could accept.
  * So [ScheduleMapper] exists to map [ScheduleView] (the type returned from [ScheduleRepository])
  * to [SchedulePojo] (the typed accepted by [ScheduleSerializer]) **/
interface ScheduleMapper {

    fun schedulePojo(view: ScheduleView): SchedulePojo


    companion object {

        fun forVersion(v: Int): ScheduleMapper {
            if(v == 1) {
                return ScheduleMapper1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}