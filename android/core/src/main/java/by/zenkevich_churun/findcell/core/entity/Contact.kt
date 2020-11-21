package by.zenkevich_churun.findcell.core.entity

import by.zenkevich_churun.findcell.core.R


/** Ways [Prisoner]s can contact each other. **/
sealed class Contact(

    /** Resource identifier for the icon
      * that represents a specific type of [Contact]. **/
    val iconRes: Int,

    /** Data such as phone number, username, etc. **/
    val data: String ) {

    class Phone(number: String):   Contact(R.drawable.ic_contact_phone, number)
    class Telegram(data: String):  Contact(R.drawable.ic_contact_telegram, data)
    class Viber(data: String):     Contact(R.drawable.ic_contact_viber, data)
    class WhatsApp(data: String):  Contact(R.drawable.ic_contact_whatsapp, data)
    class Skype(data: String):     Contact(R.drawable.ic_contact_skype, data)
    class VK(data: String):        Contact(R.drawable.ic_contact_vk, data)
    class Facebook(data: String):  Contact(R.drawable.ic_contact_fb, data)
    class Instagram(data: String): Contact(R.drawable.ic_contact_insta, data)
}