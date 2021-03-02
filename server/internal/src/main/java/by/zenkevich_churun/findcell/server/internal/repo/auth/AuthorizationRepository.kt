package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.domain.contract.auth.*
import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.PrisonerView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import javax.persistence.PersistenceException


class AuthorizationRepository: SviazenRepositiory() {

    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisonerView = prisonerDao.get(username, passwordHash)
        if(prisonerView != null) {
            return logInSuccess(prisonerView)
        }

        val usernameCount = prisonerDao.countByUsername(username)
        if(usernameCount == 0) {
            return LogInResponse.WrongUsername
        }

        return LogInResponse.WrongPassword
    }


    fun signUp(
        username: String,
        passwordHash: ByteArray,
        initialName: String
    ): SignUpResponse {

        val createdEntity = PrisonerEntity()
        createdEntity.username     = username
        createdEntity.passwordHash = passwordHash
        createdEntity.name         = initialName
        createdEntity.info         = ""

        try {
            prisonerDao.save(createdEntity)
        } catch(exc: PersistenceException) {
            // In the database there is a unique constraint on the username field.
            return SignUpResponse.UsernameTaken
        }

        val pojo = AuthorizedPrisonerPojo()
        pojo.id = createdEntity.id
        pojo.name = initialName

        return SignUpResponse.Success(pojo)
    }


    private fun logInSuccess(
        p: PrisonerView
    ): LogInResponse.Success {

        val pojo = AuthorizedPrisonerPojo()

        pojo.id = p.id
        pojo.name = p.name
        pojo.info = p.info

        for(contact in p.contactEntities) {
            when(ordinalToContactType(contact.key.typeOrdinal)) {
                Contact.Type.PHONE     -> pojo.phone     = contact.data
                Contact.Type.TELEGRAM  -> pojo.telegram  = contact.data
                Contact.Type.VIBER     -> pojo.viber     = contact.data
                Contact.Type.WHATSAPP  -> pojo.whatsapp  = contact.data
                Contact.Type.VK        -> pojo.vk        = contact.data
                Contact.Type.SKYPE     -> pojo.skype     = contact.data
                Contact.Type.FACEBOOK  -> pojo.facebook  = contact.data
                Contact.Type.INSTAGRAM -> pojo.instagram = contact.data
            }
        }

        return LogInResponse.Success(pojo)
    }
}