package by.zenkevich_churun.findcell.server.internal.dao.profile

import by.zenkevich_churun.findcell.server.internal.dao.internal.DatabaseConnection
import by.zenkevich_churun.findcell.server.internal.entity.ContactEntity
import by.zenkevich_churun.findcell.server.internal.entity.PrisonerEntity


class ProfileDao(
    private val connection: DatabaseConnection) {

    private val queryDeleteContacts = DeleteContactsQuery()


    fun update(prisoner: PrisonerEntity, contacts: List<ContactEntity>) {

        connection.withTransaction { entityMan ->

            // Update the Prisoner:
            entityMan.persist(prisoner)

            // Delete the old Contacts:
            queryDeleteContacts
                .getQuery(entityMan, prisoner.id)
                .executeUpdate()

            // Insert the new Contacts:
            for(contact in contacts) {
                entityMan.persist(contact)
            }
        }
    }
}