package by.zenkevich_churun.findcell.serial.sched.pojo


abstract class SchedulePojo {

    abstract var arestId: Int?
    abstract var passwordBase64: String?
    abstract var start: Long
    abstract var end: Long
    abstract val cells: List<CellPojo>
    abstract val periods: List<PeriodPojo>
}