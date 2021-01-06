package by.zenkevich_churun.findcell.server.protocol.serial.cellentry.abstr

import by.zenkevich_churun.findcell.serial.sched.pojo.CellEntryPojo
import by.zenkevich_churun.findcell.serial.sched.pojo.TwoCellEntriesPojo
import by.zenkevich_churun.findcell.server.protocol.serial.cellentry.v1.CellEntriesDeserializer1
import java.io.InputStream


interface CellEntriesDeserializer {
    fun deserialize(input: InputStream): CellEntryPojo
    fun deserializeTwo(input: InputStream): TwoCellEntriesPojo


    companion object {
        fun forVersion(v: Int): CellEntriesDeserializer {
            if(v == 1) {
                return CellEntriesDeserializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}