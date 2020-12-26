package by.zenkevich_churun.findcell.server.internal.repo.profile

import by.zenkevich_churun.findcell.contract.prisoner.entity.Prisoner
import by.zenkevich_churun.findcell.server.internal.dao.common.CommonDao
import by.zenkevich_churun.findcell.server.internal.dao.profile.ProfileDao
import by.zenkevich_churun.findcell.server.internal.entity.table.ContactEntity


class ProfileRepository(
    private val commonDao: CommonDao,
    private val dao: ProfileDao) {

    fun update(prisoner: Prisoner) {
        val passwordHash = prisoner.passwordHash
            ?: throw IllegalArgumentException("Missing password")
        val entity = commonDao.validateCredentials(prisoner.id, passwordHash)

        // 1. Prepare Prisoner to update:
        entity.name = prisoner.name
        entity.info = prisoner.info

        // 2. Prepare Contacts to insert:
        val contacts = prisoner.contacts.map { c ->
            ContactEntity.fromContact(c, prisoner.id)
        }

        // 3. Update the database:
        dao.update(entity, contacts)
    }
}