package by.zenkevich_churun.findcell.contract.prisoner.contract


object PrisonerContract1 {
    const val CONTACT_TYPE_PHONE         = 0.toShort()
    const val CONTACT_TYPE_TELEGRAM      = 1.toShort()
    const val CONTACT_TYPE_VIBER         = 2.toShort()
    const val CONTACT_TYPE_WHATSAPP      = 3.toShort()
    const val CONTACT_TYPE_VK            = 4.toShort()
    const val CONTACT_TYPE_FACEBOOK      = 5.toShort()
    const val CONTACT_TYPE_INSTAGRAM     = 6.toShort()
    
    const val KEY_CONTACT_PHONE          = "c${CONTACT_TYPE_PHONE}"
    const val KEY_CONTACT_TELEGRAM       = "c${CONTACT_TYPE_TELEGRAM}"
    const val KEY_CONTACT_VIBER          = "c${CONTACT_TYPE_VIBER}"
    const val KEY_CONTACT_WHATSAPP       = "c${CONTACT_TYPE_WHATSAPP}"
    const val KEY_CONTACT_VK             = "c${CONTACT_TYPE_VK}"
    const val KEY_CONTACT_FACEBOOK       = "c${CONTACT_TYPE_FACEBOOK}"
    const val KEY_CONTACT_INSTAGRAM      = "c${CONTACT_TYPE_INSTAGRAM}"
}