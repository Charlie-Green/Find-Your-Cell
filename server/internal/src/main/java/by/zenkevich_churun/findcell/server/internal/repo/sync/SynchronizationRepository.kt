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

        val relatedPair = relatedCoPrisoners(prisonerId)
        val related = relatedPair.first
        val relatedIds = relatedPair.second

        val suggested = suggestedCoPrisoners(prisonerId, relatedIds)

        val coPrisoners = related
            .toMutableList()
            .apply { addAll(suggested) }

        val jails = jailsDao.getFull()

        return SynchronizedDataView(coPrisoners, jails)
    }


    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]. **/
    private fun suggestedCoPrisoners(
        prisonerId: Int,
        excludedPrisonerIds: Collection<Int>
    ): List<CoPrisonerView> {

        // 1. Get all Periods for the specified Prisoner:
        val myPeriods = coPrisonersDao.periods(prisonerId)

        // 2. Prepare the excluded arest IDs list.
        //    Initially these are the current Prisoner's arests.
        val excludedArestIdsSet = hashSetOf<Int>()
        for(period in myPeriods) {
            excludedArestIdsSet.add(period.key.arestId)
        }

        // 3. We also add excluded Prisoners' Arests:
        excludedArestIdsSet.addAll(
            coPrisonersDao.arestIdsByPrisoners(excludedPrisonerIds.toList())
        )
        val excludedArestIds = excludedArestIdsSet.toList()

        // 3. Get Arest IDs to find potential CoPrisoners:
        val coPrisoners = hashMapOf<Int, CoPrisonerView>()
        for(period in myPeriods) {

            val jailName = jailsDao.nameOf(period.jailId)

            val arestIds = coPrisonersDao.getCoArestIds(
                period.jailId,
                period.cellNumber,
                period.key.start,
                period.key.end,
                excludedArestIds
            )

            val prisonerViews = coPrisonersDao.prisonerViewsByArests(arestIds)

            for(view in prisonerViews) {
                if(coPrisoners.containsKey(view.id)) {
                    continue
                }

                val cp = CoPrisonerView(
                    view,
                    CoPrisoner.Relation.SUGGESTED,
                    jailName,
                    period.cellNumber
                )

                coPrisoners[cp.id] = cp
            }
        }

        return coPrisoners.map {
            entry -> entry.value
        }
    }


    /** @return pair:
      *         1. [CoPrisoner]s with [CoPrisoner.relation]
      *            different from [CoPrisoner.Relation.SUGGESTED].
      *         2. [Collection] of those [CoPrisoner]'s IDs.
      *            Check for existence within the [Collection] is (in avegare) O(1). **/
    private fun relatedCoPrisoners(
        prisonerId: Int
    ): Pair< List<CoPrisonerView>, Collection<Int> > {

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
            CoPrisonerView(
                p,
                entry.relation,
                jailsDao.nameOf(entry.commonJailId),
                entry.commonCellNumber
            )
        }

        // 4. Safety: Hide contacts for non-connected CoPrisoners.
        for(coPrisoner in related) {
            if(coPrisoner.relation != CoPrisoner.Relation.CONNECTED) {
                coPrisoner.prisonerView.contactEntities = setOf()
            }
        }

        return Pair(related, idToEntryMap.keys)
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