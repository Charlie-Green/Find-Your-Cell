package by.zenkevich_churun.findcell.server.internal.repo.profile

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.server.internal.dao.contact.ContactsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.ContactEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class ProfileRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var contactsDao: ContactsDao


    fun update(prisoner: Prisoner) {
        // 1. Validate credentials:
        val passwordHash = prisoner.passwordHash
            ?: throw IllegalArgumentException("Missing password")
        val entity = validateCredentials(prisoner.id, passwordHash)

        // 2. Prepare Prisoner to update:
        entity.name = prisoner.name
        entity.info = prisoner.info

        // 3. Prepare Contacts to insert:
        val contacts = prisoner.contacts.map { c ->
            ContactEntity.fromContact(c, prisoner.id)
        }

        // 4. Update the database:
        prisonerDao.save(entity)
        contactsDao.delete(prisoner.id)
        contactsDao.saveAll(contacts)
    }
}