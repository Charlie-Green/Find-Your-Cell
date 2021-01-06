package by.zenkevich_churun.findcell.serial.sched.pojo


abstract class CellEntryPojo {
    abstract var passwordBase64: String?
    abstract var arestId: Int
    abstract var jailId: Int
    abstract var cellNumber: Short
}