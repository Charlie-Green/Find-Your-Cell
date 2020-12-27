package by.zenkevich_churun.findcell.core.model.contact

import by.zenkevich_churun.findcell.core.R
import by.zenkevich_churun.findcell.entity.Contact


/** Implementation of [Contact] to be used by UI classes. **/
class ContactModel private constructor(
    override val type: Type,
    override var data: String,
    val iconRes: Int
): Contact() {

    companion object {

        fun from(c: Contact) = ContactModel(
            c.type,
            c.data,
            iconResourceFor(c.type)
        )

        fun iconResourceFor(type: Contact.Type) = when(type) {
            Contact.Type.PHONE     -> R.drawable.ic_contact_phone
            Contact.Type.TELEGRAM  -> R.drawable.ic_contact_telegram
            Contact.Type.VIBER     -> R.drawable.ic_contact_viber
            Contact.Type.WHATSAPP  -> R.drawable.ic_contact_whatsapp
            Contact.Type.SKYPE     -> R.drawable.ic_contact_skype
            Contact.Type.VK        -> R.drawable.ic_contact_vk
            Contact.Type.FACEBOOK  -> R.drawable.ic_contact_fb
            Contact.Type.INSTAGRAM -> R.drawable.ic_contact_insta
        }
    }
}