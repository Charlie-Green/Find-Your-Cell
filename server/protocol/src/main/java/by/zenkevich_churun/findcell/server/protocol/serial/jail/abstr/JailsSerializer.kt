package by.zenkevich_churun.findcell.server.protocol.serial.jail.abstr

import by.zenkevich_churun.findcell.entity.entity.Jail


interface JailsSerializer {
    fun serialize(jails: List<Jail>): String


    companion object {

        fun forVersion(v: Int): JailsSerializer {
            if(v == 1) {
                return TODO()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}