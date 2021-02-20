package by.zenkevich_churun.findcell.server.protocol.serial.cp.abstr

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.server.protocol.serial.cp.v1.CoPrisonerSerializer1


internal interface CoPrisonerSerializer {
    fun serialize(info: String, contacts: List<Contact>): String


    companion object {

        fun forVersion(v: Int): CoPrisonerSerializer {
            if(v == 1) {
                return CoPrisonerSerializer1()
            }
            throw IllegalArgumentException("Unknown version $v")
        }
    }
}