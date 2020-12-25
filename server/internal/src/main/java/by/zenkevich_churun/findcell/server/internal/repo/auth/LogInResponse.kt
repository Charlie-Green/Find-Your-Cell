package by.zenkevich_churun.findcell.server.internal.repo.auth

import by.zenkevich_churun.findcell.server.internal.entity.ContactEntity
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity


sealed class LogInResponse {

    class Success(
        val prisoner: PrisonerEntity,
        val contacts: List<ContactEntity>
    ): LogInResponse()

    object WrongUsername: LogInResponse()
    object WrongPassword: LogInResponse()
}