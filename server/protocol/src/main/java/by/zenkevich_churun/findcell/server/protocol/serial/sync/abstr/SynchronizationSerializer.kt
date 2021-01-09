package by.zenkevich_churun.findcell.server.protocol.serial.sync.abstr

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.entity.Jail
import by.zenkevich_churun.findcell.server.protocol.serial.sync.v1.SynchronizationSerializer1


interface SynchronizationSerializer {
    fun serialize(
        coPrisoners: List<CoPrisoner>,
        jails: List<Jail>
    ): String


    companion object {

        fun forVersion(v: Int): SynchronizationSerializer {
            if(v == 1) {
                return SynchronizationSerializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}