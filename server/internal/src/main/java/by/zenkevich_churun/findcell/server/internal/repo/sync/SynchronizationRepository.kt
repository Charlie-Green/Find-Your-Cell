package by.zenkevich_churun.findcell.server.internal.repo.sync

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import by.zenkevich_churun.findcell.server.internal.dao.cp.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.CoPrisonerView
import by.zenkevich_churun.findcell.server.internal.entity.view.SynchronizedDataView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class SynchronizationRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var jailsDao: JailsDao

    @Autowired
    private lateinit var coPrisonersDao: CoPrisonersDao


    fun synchronizedData(
        prisonerId: Int,
        passwordHash: ByteArray
    ): SynchronizedPojo {

        validateCredentials(prisonerId, passwordHash)

        val coPrisoners = suggestedCoPrisoners(prisonerId)
            .toMutableList()
            .apply { addAll( relatedCoPrisoners(prisonerId) ) }

        val jails = jailsDao.getFull()

        return SynchronizedDataView(coPrisoners, jails)
    }


    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]. **/
    private fun suggestedCoPrisoners(prisonerId: Int): List<CoPrisonerView> {

        // 1. Get all Periods for the specified Prisoner:
        val myPeriods = coPrisonersDao.periods(prisonerId)

        // 2. Prepare the excluded arest IDs list:
        val excludedArestIdsSet = hashSetOf<Int>()
        for(period in myPeriods) {
            excludedArestIdsSet.add(period.key.arestId)
        }
        val excludedArestIds = excludedArestIdsSet.toList()

        // 3. Get Arest IDs to find potential CoPrisoners:
        val othersArestIds = hashSetOf<Int>()
        for(period in myPeriods) {
            val ids = coPrisonersDao.getCoArestIds(
                period.jailId,
                period.cellNumber,
                period.key.start,
                period.key.end,
                excludedArestIds
            )

            othersArestIds.addAll(ids)
        }

        return coPrisonersDao
            .coPrisonersByArests(othersArestIds.toList())
            .map { scp -> scp.toCoPrisonerView() }
    }


    /** [CoPrisoner]s with [CoPrisoner.relation]
      * different from [CoPrisoner.Relation.SUGGESTED]. **/
    private fun relatedCoPrisoners(prisonerId: Int): List<CoPrisonerView> {

        // 1. Get desired Relation entries:
        val relatedEntries = coPrisonersDao.coPrisonerEntries(prisonerId)

        // 2. Prepare Prisoner IDs and ID-to-Entry map:
        val idToEntryMap = hashMapOf<Int, CoPrisonerEntity>()
        val relatedIds = relatedEntries.map { entry ->
            coPrisonerId(entry, prisonerId, idToEntryMap)
        }

        // 3. Map this to CoPrisonerView entities:
        val relatedPrisonerViews = coPrisonersDao.prisonerViews(relatedIds)
        val related = relatedPrisonerViews.map { p ->
            val entry = idToEntryMap[p.id]!!
            CoPrisonerView(p, entry.relation)
        }

        // 4. Safety: Hide contacts for non-connected CoPrisoners.
        for(coPrisoner in related) {
            if(coPrisoner.relation != CoPrisoner.Relation.CONNECTED) {
                coPrisoner.prisonerView.contactEntities = setOf()
            }
        }

        return related
    }

    private fun coPrisonerId(
        entry: CoPrisonerEntity,
        prisonerId: Int,
        idToEntryMap: HashMap<Int, CoPrisonerEntity>
    ): Int {

        val id =
            if(entry.key.id1 == prisonerId) entry.key.id2
            else entry.key.id1

        idToEntryMap[id] = entry

        return id
    }
}