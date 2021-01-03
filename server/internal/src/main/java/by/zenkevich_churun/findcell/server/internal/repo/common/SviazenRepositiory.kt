package by.zenkevich_churun.findcell.server.internal.repo.common

import by.zenkevich_churun.findcell.server.internal.dao.prisoner.PrisonerDao
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
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
}