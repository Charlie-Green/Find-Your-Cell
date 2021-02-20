package by.zenkevich_churun.findcell.serial.sched.pojo

import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod


abstract class PeriodPojo: SchedulePeriod() {

    // Was immutable, becomes mutable
    abstract override var cellIndex: Int
}