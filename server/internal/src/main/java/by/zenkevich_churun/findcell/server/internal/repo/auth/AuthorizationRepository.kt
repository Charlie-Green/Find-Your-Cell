package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.entity.response.LogInResponse
import by.zenkevich_churun.findcell.entity.response.SignUpResponse
import by.zenkevich_churun.findcell.server.internal.dao.contact.ContactsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.PrisonerView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.PersistenceException


class AuthorizationRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var contactsDao: ContactsDao


    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisonerEntity = prisonerDao.get(username, passwordHash)
        if(prisonerEntity != null) {
            val contacts = contactsDao.get(prisonerEntity.id)
            prisonerEntity.passwordHash = null
            val prisonerInstance = PrisonerView(prisonerEntity, contacts)
            return LogInResponse.Success(prisonerInstance)
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

        val createdPrisoner = PrisonerView(createdEntity, listOf())
        return SignUpResponse.Success(createdPrisoner)
    }
}