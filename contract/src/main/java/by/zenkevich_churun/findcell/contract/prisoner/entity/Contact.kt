package by.zenkevich_churun.findcell.contract.prisoner.entity


abstract class Contact {

    /** Which messenger it is, or it is a phone number, or what. **/
    abstract val type: Contact.Type

    /** Data such as phone number, username, etc. **/
    abstract val data: String


    enum class Type {
        PHONE,
        TELEGRAM,
        VIBER,
        WHATSAPP,
        SKYPE,
        VK,
        FACEBOOK,
        INSTAGRAM
    }
}