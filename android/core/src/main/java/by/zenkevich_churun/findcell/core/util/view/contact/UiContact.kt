package by.zenkevich_churun.findcell.core.util.view.contact

import by.zenkevich_churun.findcell.core.R
import by.zenkevich_churun.findcell.entity.Contact


/** Implementation of [Contact] internally used by [ContactView]. **/
internal class UiContact(
    override val type: Type,
    override var data: String,
    val iconRes: Int
): Contact() {

    companion object {

        fun from(c: Contact): UiContact {
            if(c is UiContact) {
                return c
            }

            val iconRes = when(c.type) {
                Contact.Type.PHONE     -> R.drawable.ic_contact_phone
                Contact.Type.TELEGRAM  -> R.drawable.ic_contact_telegram
                Contact.Type.VIBER     -> R.drawable.ic_contact_viber
                Contact.Type.WHATSAPP  -> R.drawable.ic_contact_whatsapp
                Contact.Type.SKYPE     -> R.drawable.ic_contact_skype
                Contact.Type.VK        -> R.drawable.ic_contact_vk
                Contact.Type.FACEBOOK  -> R.drawable.ic_contact_fb
                Contact.Type.INSTAGRAM -> R.drawable.ic_contact_insta
            }

            return UiContact(c.type, c.data, iconRes)
        }
    }
}