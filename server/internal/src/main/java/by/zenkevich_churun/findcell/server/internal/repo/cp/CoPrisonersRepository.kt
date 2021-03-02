package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.domain.contract.cp.CoPrisonerDataPojo
import by.zenkevich_churun.findcell.domain.entity.*
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.cp.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.entity.key.CellKey
import by.zenkevich_churun.findcell.server.internal.entity.key.CoPrisonerKey
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class CoPrisonersRepository: SviazenRepositiory() {
    // ==============================================================================
    // Fields:

    @Autowired
    private lateinit var dao: CoPrisonersDao

    @Autowired
    private lateinit var arestsDao: ArestsDao


    // ==============================================================================
    // Public:

    /** @return the new [CoPrisoner.Relation] between the 2 [Prisoner]s. **/
    fun connect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        validateCredentials(prisonerId, passwordHash)

        // Check if these users are already related:
        var record = dao.coPrisoner(prisonerId, coPrisonerId)
        if(record != null) {
            return connect(prisonerId, record)
        }

        // Check if the users are suggested to each other:
        val interceptCell = findCommonCell(prisonerId, coPrisonerId)
        if(interceptCell != null) {
            record = CoPrisonerEntity()
            record.key = CoPrisonerKey()
            record.key.id1 = prisonerId
            record.key.id2 = coPrisonerId
            record.commonJailId = interceptCell.jailId
            record.commonCellNumber = interceptCell.cellNumber
            record.relationOrdinal = CoPrisoner.Relation.SUGGESTED.ordinal.toShort()

            return connect(prisonerId, record)
        }

        throwNotCoprisoners(prisonerId, coPrisonerId)
    }

    /** @return the new [CoPrisoner.Relation] between the 2 [Prisoner]s. **/
    fun disconnect(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisoner.Relation {

        validateCredentials(prisonerId, passwordHash)

        // If the users were partly or fully connected,
        // break one side of the connection:
        val record = dao.coPrisoner(prisonerId, coPrisonerId)
        if(record != null) {
            val firstBreaks = (prisonerId == record.key.id1)
            return disconnect(record, firstBreaks)
        }

        throwNotCoprisoners(prisonerId, coPrisonerId)
    }


    /** @return [Prisoner] entity with [Prisoner.info] and [Prisoner.contacts]
      * of the requested [CoPrisoner], or null if the specified [Prisoner]s
      * are not connected.**/
    fun coPrisoner(
        prisonerId: Int,
        passwordHash: ByteArray,
        coPrisonerId: Int
    ): CoPrisonerDataPojo? {

        validateCredentials(prisonerId, passwordHash)

        val entry = dao.coPrisoner(prisonerId, coPrisonerId)
        if( entry?.relationOrdinal !=
            CoPrisoner.Relation.CONNECTED.ordinal.toShort()) {
            return null
        }

        val prisoner = prisonerDao.get(coPrisonerId)

        val pojo = CoPrisonerDataPojo()
        pojo.info = prisoner.info
        for(contact in prisoner.contactEntities) {
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

        return pojo
    }


    // ==============================================================================
    // Private (Major functionality)

    /** @return [CellKey] containing [Jail] ID and [Cell] number
      * of the [Prisoner]s' common cell, if there is one, or null. **/
    private fun findCommonCell(id1: Int, id2: Int): CellKey? {
        // Select all Periods for Prisoner #1:
        val myPeriods = dao.periods(id1)

        // Select all Arests for Prisoner #2:
        val theirArests = arestsDao.arestIds(id2)

        for(p in myPeriods) {
            // Check if this Period intersects with any of Prisoner #2's:
            val intersectCount = dao.countIntersections(
                theirArests,
                p.key.start, p.key.end,
                p.jailId, p.cellNumber
            )

            if(intersectCount != 0) {
                val result = CellKey()
                result.jailId = p.jailId
                result.cellNumber = p.cellNumber
                return result
            }
        }

        return null
    }


    /** @param firstConnects whether the user willing to connect is the first one. **/
    private fun connect(
        id1: Int,
        record: CoPrisonerEntity
    ): CoPrisoner.Relation {

        val relationCode = RelationCode.encode(record.relationOrdinal)
        if(id1 == record.key.id1) {
            relationCode.set1()
        } else {
            relationCode.set2()
        }

        record.relationOrdinal = relationCode.decode()
        dao.save(record)

        return RelationResolver(record.relationOrdinal)
            .resolve(id1 == record.key.id1)
    }

    /** @param firstBreaks whether [record.id1] is the ID
      * of the user who is breaking the connection. **/
    private fun disconnect(
        record: CoPrisonerEntity,
        firstBreaks: Boolean
    ): CoPrisoner.Relation {

        val relationCode = RelationCode.encode(record.relationOrdinal)
        if(firstBreaks) {
            relationCode.unset1()
        } else {
            relationCode.unset2()
        }
        record.relationOrdinal = relationCode.decode()

        dao.save(record)

        return RelationResolver(record.relationOrdinal).resolve(firstBreaks)
    }


    // ==============================================================================
    // Private (Help):

    private fun throwNotCoprisoners(
        id1: Int,
        id2: Int
    ): Nothing = throw IllegalArgumentException(
        "Prisoners $id1 and $id2 are not co-prisoners"
    )
}