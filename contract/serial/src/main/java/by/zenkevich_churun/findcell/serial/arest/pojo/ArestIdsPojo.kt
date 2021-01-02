package by.zenkevich_churun.findcell.serial.arest.pojo


abstract class ArestIdsPojo {
    abstract var prisonerId: Int
    abstract var passwordHash: ByteArray
    abstract var arestIds: List<Int>
}