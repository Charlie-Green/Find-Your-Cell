package by.zenkevich_churun.findcell.server.protocol.encode.arest

import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView


interface ArestsEncoder {
    fun encode(arests: List<ArestView>): String


    companion object {

        fun forVersion(v: Int): ArestsEncoder = when(v) {
            1 -> ArestsEncoder1()
            else -> throw IllegalArgumentException("Unknown protocol version $v")
        }
    }
}