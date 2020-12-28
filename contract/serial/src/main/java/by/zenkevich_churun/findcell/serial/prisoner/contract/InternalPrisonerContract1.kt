package by.zenkevich_churun.findcell.serial.prisoner.contract

import by.zenkevich_churun.findcell.entity.entity.Contact


internal object InternalPrisonerContract1 {
    const val KEY_PRISONER_ID            = "id"
    const val KEY_PRISONER_USERNAME      = "uname"
    const val KEY_PRISONER_PASSWORD_HASH = "pass"
    const val KEY_PRISONER_NAME          = "name"
    const val KEY_PRISONER_INFO          = "info"

    fun keyContact(type: Contact.Type): String
        = "c${type.ordinal}"
}