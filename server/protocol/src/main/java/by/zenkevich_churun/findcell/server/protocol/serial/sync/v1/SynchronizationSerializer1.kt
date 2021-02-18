package by.zenkevich_churun.findcell.server.protocol.serial.sync.v1

import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import by.zenkevich_churun.findcell.serial.sync.v1.SynchronizedPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.server.protocol.serial.sync.abstr.SynchronizationSerializer


class SynchronizationSerializer1: SynchronizationSerializer {

    override fun serialize(data: SynchronizedPojo): String {
        val pojo = SynchronizedPojo1.from(data)
        val approxSize = 64 + 128*pojo.coPrisoners.size + 512*pojo.jails.size
        return ProtocolUtil.toJson(pojo, approxSize)
    }
}