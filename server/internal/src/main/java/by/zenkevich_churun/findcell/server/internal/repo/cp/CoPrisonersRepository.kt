package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.server.internal.dao.cp.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.entity.key.CellKey
import by.zenkevich_churun.findcell.server.internal.entity.key.CoPrisonerKey
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class CoPrisonersRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var dao: CoPrisonersDao


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
            return connect(
                prisonerId,
                coPrisonerId,
                record
            )
        }

        // Check if the users are suggested to each other:
        val interceptCell = areUsersSuggested(prisonerId, coPrisonerId)
        if(interceptCell != null) {
            record = CoPrisonerEntity()
            record.key = CoPrisonerKey()
            record.key.id1 = prisonerId
            record.key.id2 = coPrisonerId
            record.commonJailId = interceptCell.jailId
            record.commonCellNumber = interceptCell.cellNumber
            record.relation = CoPrisoner.Relation.SUGGESTED

            return connect(
                prisonerId,
                coPrisonerId,
                record
            )
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


    /** @return [CellKey] containing [Jail] ID and [Cell] number
      * of the [Prisoner]s' common cell, if there is one, or null. **/
    private fun areUsersSuggested(id1: Int, id2: Int): CellKey? {
        // Select all Periods for Prisoner #1.
        val periods = dao.periods(id1)

        for(p in periods) {
            // Check if this Period intersects with any of Prisoner #2's:
            // TODO: Pre-fetch arest IDs.
            val intersectCount = dao.countIntersections(
                id2,
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
        id2: Int,
        record: CoPrisonerEntity
    ): CoPrisoner.Relation {

        val relationCode = RelationCode.encode(record.relation)
        if(id1 == record.key.id1) {
            relationCode.set1()
        } else {
            relationCode.set2()
        }

        record.relation = relationCode.decode()
        dao.save(record)

        return record.relation
    }

    /** @param firstBreaks whether [record.id1] is the ID
      * of the user who is breaking the connection. **/
    private fun disconnect(
        record: CoPrisonerEntity,
        firstBreaks: Boolean
    ): CoPrisoner.Relation {

        val relationCode = RelationCode.encode(record.relation)
        if(firstBreaks) {
            relationCode.unset1()
        } else {
            relationCode.unset2()
        }
        record.relation = relationCode.decode()

        dao.save(record)

        return record.relation
    }


    private fun throwNotCoprisoners(
        id1: Int,
        id2: Int
    ): Nothing = throw IllegalArgumentException("Prisoners $id1 and $id2 are not co-prisoners")
}