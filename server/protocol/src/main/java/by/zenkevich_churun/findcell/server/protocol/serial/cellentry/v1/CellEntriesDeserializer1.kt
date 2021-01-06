package by.zenkevich_churun.findcell.server.protocol.serial.cellentry.v1

import by.zenkevich_churun.findcell.serial.sched.pojo.CellEntryPojo
import by.zenkevich_churun.findcell.serial.sched.v1.pojo.CellEntryPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.server.protocol.serial.cellentry.abstr.CellEntriesDeserializer
import java.io.InputStream


internal class CellEntriesDeserializer1: CellEntriesDeserializer {

    override fun deserialize(input: InputStream): CellEntryPojo {
        return ProtocolUtil.fromJson(input, CellEntryPojo1::class.java)
    }
}