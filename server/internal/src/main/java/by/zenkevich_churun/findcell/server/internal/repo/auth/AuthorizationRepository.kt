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

    fun logIn(
        username: String,
        passwordHash: ByteArray
    ): LogInResponse {

        val prisonerView = prisonerDao.get(username, passwordHash)
        if(prisonerView != null) {
            return LogInResponse.Success(prisonerView)
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

        val createdPrisoner = PrisonerView()
        createdPrisoner.id         = createdEntity.id
        createdEntity.username     = createdEntity.username
        createdEntity.passwordHash = createdEntity.passwordHash
        createdEntity.name         = createdEntity.name
        createdEntity.info         = createdEntity.info

        return SignUpResponse.Success(createdPrisoner)
    }
}