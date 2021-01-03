package by.zenkevich_churun.findcell.serial.sched.pojo

import by.zenkevich_churun.findcell.entity.entity.Schedule


abstract class SchedulePojo: Schedule() {
    abstract var passwordBase64: String?
}