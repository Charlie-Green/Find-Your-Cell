package by.zenkevich_churun.findcell.remote.lan.auth.entity

import android.util.Log
import by.zenkevich_churun.findcell.contract.prisoner.contract.PrisonerContract1
import by.zenkevich_churun.findcell.core.entity.general.Contact
import by.zenkevich_churun.findcell.core.entity.general.Prisoner


internal class RetrofitPrisoner(
    override val id: Int,
    override val name: String,
    override val info: String,
    override val contacts: List<Contact>
): Prisoner() {

    companion object {
        fun from(p: by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner): RetrofitPrisoner {

            val mappedContacts = mutableListOf<Contact>()
            for(rawContact in p.contacts) {
                contactFrom(rawContact)?.also {
                    mappedContacts.add(it)
                }
            }

            return RetrofitPrisoner(
                p.id,
                p.name,
                p.info,
                mappedContacts
            )
        }


        private fun contactFrom(c: by.zenkevich_churun.findcell.contract.prisoner.entity.Contact): Contact? {
            return when(c.type) {
                PrisonerContract1.CONTACT_TYPE_PHONE     -> Contact.Phone(c.data)
                PrisonerContract1.CONTACT_TYPE_TELEGRAM  -> Contact.Telegram(c.data)
                PrisonerContract1.CONTACT_TYPE_VIBER     -> Contact.Viber(c.data)
                PrisonerContract1.CONTACT_TYPE_WHATSAPP  -> Contact.WhatsApp(c.data)
                PrisonerContract1.CONTACT_TYPE_VK        -> Contact.VK(c.data)
                PrisonerContract1.CONTACT_TYPE_FACEBOOK  -> Contact.Facebook(c.data)
                PrisonerContract1.CONTACT_TYPE_INSTAGRAM -> Contact.Instagram(c.data)

                else -> {
                    Log.e("FindCell-Auth", "Unknown contact type ${c.type}")
                    null
                }
            }
        }
    }
}