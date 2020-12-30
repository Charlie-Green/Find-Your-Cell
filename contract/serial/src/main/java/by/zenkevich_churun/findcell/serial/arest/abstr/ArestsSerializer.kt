package by.zenkevich_churun.findcell.serial.arest.abstr

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.serial.arest.v1.serial.ArestsSerializer1


interface ArestsSerializer {
    fun serialize(arests: List<LightArest>): String

    fun serialize(
        arest: LightArest,
        prisonerId: Int,
        passwordHash: ByteArray
    ): String


    companion object {

        fun forVersion(v: Int): ArestsSerializer {
            if(v == 1) {
                return ArestsSerializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}