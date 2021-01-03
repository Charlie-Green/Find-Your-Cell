package by.zenkevich_churun.findcell.serial.sched.pojo


abstract class SchedulePojo {

    abstract var arestId: Int?
    abstract var passwordBase64: String?
    abstract var start: Long
    abstract var end: Long
    abstract var cells: List<CellPojo>
    abstract var periods: List<PeriodPojo>
}