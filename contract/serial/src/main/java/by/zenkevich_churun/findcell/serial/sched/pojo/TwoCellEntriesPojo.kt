package by.zenkevich_churun.findcell.serial.sched.pojo


abstract class TwoCellEntriesPojo {
    abstract var arestId: Int
    abstract var passwordBase64: String?
    abstract var oldJailId: Int
    abstract var oldCellNumber: Short
    abstract var newJailId: Int
    abstract var newCellNumber: Short
}