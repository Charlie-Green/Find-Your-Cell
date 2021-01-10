package by.zenkevich_churun.findcell.server.internal.repo.cp

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.server.internal.dao.cp.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.entity.key.CoPrisonerKey
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.Calendar


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
        val record = dao.coPrisoner(prisonerId, coPrisonerId)
        if(record != null) {
            return connect(prisonerId, coPrisonerId, record.relation)
        }

        // Check if the users are suggested to each other:
        if(areUsersSuggested(prisonerId, coPrisonerId)) {
            return connect(prisonerId, coPrisonerId, CoPrisoner.Relation.SUGGESTED)
        }

        throw IllegalArgumentException(
            "Prisoners $prisonerId and $coPrisonerId are not related nor suggested" )
    }


    private fun areUsersSuggested(id1: Int, id2: Int): Boolean {
        // Select all Periods for Prisoner #1.
        val periods = dao.periods(id1)

        for(p in periods) {
            // Check if this Period intersects with any of Prisoner #2's:
            val intersectCount = dao.countIntersections(
                id2,
                p.key.start, p.key.end,
                p.jailId, p.cellNumber
            )

            // TODO: Pre-fetch arest IDs.
            val charlieDebugDateFormat = SimpleDateFormat("dd.MM.yyyy")
            val start = charlieDebugDateFormat.format( Calendar.getInstance().apply { timeInMillis = p.key.start }.time )
            val end   = charlieDebugDateFormat.format( Calendar.getInstance().apply { timeInMillis = p.key.end   }.time )
            println("$start - $end has $intersectCount intersections")

            if(intersectCount != 0) {
                return true
            }
        }

        return false
    }

    private fun connect(
        id1: Int,
        id2: Int,
        currentRelation: CoPrisoner.Relation
    ): CoPrisoner.Relation {

        val newRelation = when(currentRelation) {
            CoPrisoner.Relation.SUGGESTED         -> CoPrisoner.Relation.OUTCOMING_REQUEST
            CoPrisoner.Relation.INCOMING_REQUEST  -> CoPrisoner.Relation.CONNECTED
            else                                  -> currentRelation
        }

        val entity = CoPrisonerEntity()
        entity.key = CoPrisonerKey()
        entity.key.id1 = id1
        entity.key.id2 = id2
        entity.relation = newRelation

        dao.save(entity)

        return newRelation
    }
}