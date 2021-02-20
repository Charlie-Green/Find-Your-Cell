package by.zenkevich_churun.findcell.server.protocol.serial.cp.v1

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.serial.cp.v1.CoPrisonerContactsPojo1
import by.zenkevich_churun.findcell.serial.util.protocol.ProtocolUtil
import by.zenkevich_churun.findcell.server.protocol.serial.cp.abstr.CoPrisonerSerializer


internal class CoPrisonerSerializer1: CoPrisonerSerializer {

    override fun serialize(
        info: String,
        contacts: List<Contact>
    ): String {

        val pojo = CoPrisonerContactsPojo1()
        pojo.info = info

        for(contact in contacts) {
            val data = contact.data
            when(contact.type) {
                Contact.Type.PHONE     -> pojo.contactPhone     = data
                Contact.Type.TELEGRAM  -> pojo.contactTelegram  = data
                Contact.Type.VIBER     -> pojo.contactViber     = data
                Contact.Type.WHATSAPP  -> pojo.contactWhatsApp  = data
                Contact.Type.VK        -> pojo.contactVk        = data
                Contact.Type.FACEBOOK  -> pojo.contactFacebook  = data
                Contact.Type.INSTAGRAM -> pojo.contactInstagram = data
                Contact.Type.SKYPE     -> pojo.contactSkype     = data
            }
        }

        val approxSize = info.length + 42*contacts.size + 4
        return ProtocolUtil.toJson(pojo, approxSize)
    }
}