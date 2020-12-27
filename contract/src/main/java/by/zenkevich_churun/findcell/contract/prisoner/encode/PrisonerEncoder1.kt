package by.zenkevich_churun.findcell.contract.prisoner.encode

import by.zenkevich_churun.findcell.contract.prisoner.entity.Contact
import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner
import by.zenkevich_churun.findcell.contract.prisoner.internal.InternalPrisonerContract1
import by.zenkevich_churun.findcell.contract.prisoner.util.ProtocolUtil


/** Converts a Prisoner into JSON format. **/
internal class PrisonerEncoder1: PrisonerEncoder {

    override fun encode(prisoner: Prisoner): String {
        val sb = StringBuilder(
            "{" +
                "\"${InternalPrisonerContract1.KEY_PRISONER_ID  }\": \"${prisoner.id}\",\n" +
                "\"${InternalPrisonerContract1.KEY_PRISONER_NAME}\": \"${prisoner.name}\",\n" +
                "\"${InternalPrisonerContract1.KEY_PRISONER_INFO}\": \"${prisoner.info}\""
        )

        for(c in prisoner.contacts) {
            sb.append(",\n")
            sb.append( encodeContact(c) )
        }

        prisoner.username?.also { username ->
            val key = InternalPrisonerContract1.KEY_PRISONER_USERNAME
            sb.append(",\n\"$key\": \"$username\"")
        }

        // Binary password hash is converted to Base64
        // to be transmitted in a text format.
        prisoner.passwordHash?.also { hash ->
            val base64 = ProtocolUtil.encodeBase64(hash)
            val key = InternalPrisonerContract1.KEY_PRISONER_PASSWORD_HASH
            sb.append(",\n\"$key\": \"$base64\"")
        }

        sb.append("}")
        return sb.toString()
    }


    private fun encodeContact(c: Contact): String {
        return "\"${InternalPrisonerContract1.keyContact(c.type)}\": \"${c.data}\""
    }
}