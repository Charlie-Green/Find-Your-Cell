package by.sviazen.server.internal.repo.common

import by.sviazen.domain.entity.Contact
import by.sviazen.server.internal.dao.prisoner.PrisonerDao
import by.sviazen.server.internal.entity.table.PrisonerEntity
import org.springframework.beans.factory.annotation.Autowired


/** Abstract class to implement some common scenarios for all of the app's repositories. **/
abstract class SviazenRepositiory {

    // Needed by descendants to validate Prisoner credentials:
    @Autowired
    protected lateinit var prisonerDao: PrisonerDao


    protected fun validateCredentials(
        prisonerId: Int,
        passwordHash: ByteArray
    ): PrisonerEntity {

        return prisonerDao
            .get(prisonerId, passwordHash)
            ?: throw IllegalArgumentException("Invalid credentials. prisonerId=$prisonerId")
    }


    protected fun ordinalToContactType(ordinal: Short): Contact.Type
        = Contact.Type.values()[ordinal.toInt()]

    protected fun contactTypeToOrdinal(type: Contact.Type): Short
        = type.ordinal.toShort()
}