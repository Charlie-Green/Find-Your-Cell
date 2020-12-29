package by.zenkevich_churun.findcell.server.protocol.serial.arest.abstr

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.server.protocol.serial.arest.v1.ArestsSerializer1


interface ArestsSerializer {
    fun serialize(arests: List<LightArest>): String


    companion object {

        fun forVersion(v: Int): ArestsSerializer {
            if(v == 1) {
                return ArestsSerializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}