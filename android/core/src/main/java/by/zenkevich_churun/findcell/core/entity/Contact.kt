package by.zenkevich_churun.findcell.core.entity

import by.zenkevich_churun.findcell.core.R


/** Ways [Prisoner]s can contact each other. **/
class Contact(

    /** Which messenger it is, or it is a phone number, or what. **/
    val type: Contact.Type,

    /** Data such as phone number, username, etc. **/
    val data: String ) {


    enum class Type(
        /** Resource identifier for the icon
          * that represents a specific type of [Contact]. **/
        val iconRes: Int ) {

        PHONE(R.drawable.ic_contact_phone),
        TELEGRAM(R.drawable.ic_contact_telegram),
        VIBER(R.drawable.ic_contact_viber),
        WHATSAPP(R.drawable.ic_contact_whatsapp),
        SKYPE(R.drawable.ic_contact_skype),
        VK(R.drawable.ic_contact_vk),
        FACEBOOK(R.drawable.ic_contact_fb),
        INSTAGRAM(R.drawable.ic_contact_insta)
    }
}