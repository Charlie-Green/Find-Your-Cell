package by.zenkevich_churun.findcell.server.protocol.encode

import by.zenkevich_churun.findcell.server.internal.entity.ContactEntity
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.repo.auth.LogInResponse


internal class AuthorizationEncoder1: AuthorizationEncoder {

    private val KEY_PRISONER_ID  = "id"
    private val KEY_PRISONER_NAME = "name"
    private val KEY_PRISONER_INFO = "info"
    private val KEY_CONTACTS      = "contacts"

    private val CONTACT_TYPE_PHONE     = 0
    private val CONTACT_TYPE_TELEGRAM  = 1
    private val CONTACT_TYPE_VIBER     = 2
    private val CONTACT_TYPE_WHATSAPP  = 3
    private val CONTACT_TYPE_VK        = 4
    private val CONTACT_TYPE_FACEBOOK  = 5
    private val CONTACT_TYPE_SKYPE     = 6
    private val CONTACT_TYPE_INSTAGRAM = 7


    override fun encode(
        response: LogInResponse
    ): String = when(response) {

        is LogInResponse.Success       -> encodePrisoner(response.prisoner, response.contacts)
        is LogInResponse.WrongUsername -> "U"
        is LogInResponse.WrongPassword -> "P"
    }


    private fun encodePrisoner(
        prisoner: PrisonerEntity,
        contacts: List<ContactEntity>
    ): String {

        val sb = StringBuilder(
            "{" +
                "\"${KEY_PRISONER_ID  }\": \"${prisoner.id}\"\n" +
                "\"${KEY_PRISONER_NAME}\": \"${prisoner.name}\"\n" +
                "\"${KEY_PRISONER_INFO}\": \"${prisoner.info}\"\n" +
                "\"${KEY_CONTACTS     }\": ["
        )

        for(c in contacts) {
            sb.append('\n')
            sb.append( encodeContact(c) )
        }
        sb.append("]}")

        return sb.toString()
    }


    private fun encodeContact(c: ContactEntity): String {
        return "\"${c.key.type}\": \"${c.data}\""
    }
}