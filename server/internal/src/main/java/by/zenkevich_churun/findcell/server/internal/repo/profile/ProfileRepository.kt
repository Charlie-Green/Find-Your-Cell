package by.zenkevich_churun.findcell.server.internal.repo.profile

import by.zenkevich_churun.findcell.domain.contract.prisoner.UpdatedPrisonerPojo
import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.server.internal.dao.contact.ContactsDao
import by.zenkevich_churun.findcell.server.internal.entity.key.ContactKey
import by.zenkevich_churun.findcell.server.internal.entity.table.ContactEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class ProfileRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var contactsDao: ContactsDao


    fun update(prisoner: UpdatedPrisonerPojo, passwordHash: ByteArray) {
        // 1. Validate credentials:
        val entity = validateCredentials(prisoner.id, passwordHash)

        // 2. Prepare Prisoner to update:
        entity.name = prisoner.name
        entity.info = prisoner.info

        // 3. Prepare Contacts to insert:
        val contacts = mutableListOf<ContactEntity>()
        addContactEntity(contacts, prisoner.id, prisoner.phone,     Contact.Type.PHONE)
        addContactEntity(contacts, prisoner.id, prisoner.telegram,  Contact.Type.TELEGRAM)
        addContactEntity(contacts, prisoner.id, prisoner.viber,     Contact.Type.VIBER)
        addContactEntity(contacts, prisoner.id, prisoner.whatsapp,  Contact.Type.WHATSAPP)
        addContactEntity(contacts, prisoner.id, prisoner.vk,        Contact.Type.VK)
        addContactEntity(contacts, prisoner.id, prisoner.skype,     Contact.Type.SKYPE)
        addContactEntity(contacts, prisoner.id, prisoner.facebook,  Contact.Type.FACEBOOK)
        addContactEntity(contacts, prisoner.id, prisoner.instagram, Contact.Type.INSTAGRAM)

        // 4. Update the database:
        prisonerDao.save(entity)
        contactsDao.delete(prisoner.id)
        contactsDao.saveAll(contacts)
    }


    private fun addContactEntity(
        target: MutableCollection<ContactEntity>,
        prisonerId: Int,
        data: String?,
        type: Contact.Type ) {

        data ?: return
        val key = ContactKey(prisonerId, type.ordinal.toShort())
        val entity = ContactEntity(key, data)

        target.add(entity)
    }
}