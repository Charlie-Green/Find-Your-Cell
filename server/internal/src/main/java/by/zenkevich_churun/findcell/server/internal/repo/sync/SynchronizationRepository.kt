package by.zenkevich_churun.findcell.server.internal.repo.sync

import by.zenkevich_churun.findcell.domain.contract.cp.CoPrisonerHeaderPojo
import by.zenkevich_churun.findcell.domain.contract.jail.FullJailPojo
import by.zenkevich_churun.findcell.domain.contract.jail.JailPojo
import by.zenkevich_churun.findcell.domain.contract.sync.SynchronizedPojo
import by.zenkevich_churun.findcell.domain.entity.CoPrisoner
import by.zenkevich_churun.findcell.server.internal.dao.cp.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.table.JailEntity
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import by.zenkevich_churun.findcell.server.internal.repo.cp.RelationResolver
import org.springframework.beans.factory.annotation.Autowired


class SynchronizationRepository: SviazenRepositiory() {
    // ==============================================================================
    // Fields:

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
        println("${suggested.size} suggested + ${related.size} related")

        val coPrisoners = related
            .toMutableList()
            .apply { addAll(suggested) }

        val fullJails = jailsDao.getFull()
        val jailPojos = fullJails.map { j ->
            val seatCounts = jailsDao.getSeatCounts(j.id)
            FullJailPojo(j.id, j.name, seatCounts)
        }

        return SynchronizedPojo(coPrisoners, jailPojos)
    }


    /** [CoPrisoner]s with [CoPrisoner.Relation.SUGGESTED]. **/
    private fun suggestedCoPrisoners(
        prisonerId: Int,
        excludedPrisonerIds: Collection<Int>
    ): List<CoPrisonerHeaderPojo> {

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

        val jailIds = myPeriods.map { period ->
            period.jailId
        }
        val jailMap = jailMap(jailIds)

        // 4. Get Arest IDs to find potential CoPrisoners:
        val coPrisoners = hashMapOf<Int, CoPrisonerHeaderPojo>()
        for(period in myPeriods) {

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

                val cp = CoPrisonerHeaderPojo(
                    view.id,
                    view.name,
                    jailMap[period.jailId]!!.name,
                    period.cellNumber,
                    CoPrisoner.Relation.SUGGESTED.ordinal.toShort()
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
    ): Pair< List<CoPrisonerHeaderPojo>, Collection<Int> > {

        // 1. Get desired Relation entries:
        val cpEntries = coPrisonersDao.coPrisonerEntries(prisonerId)

        // 2. Prepare Prisoner IDs and ID-to-Entry map:
        val idToEntryMap = hashMapOf<Int, CoPrisonerEntity>()
        val cpIds = cpEntries.map { entry ->
            coPrisonerId(entry, prisonerId, idToEntryMap)
        }

        // 3. Optimization: load Jails into a HashMap so  we don't have
        // to execute a database query on each iteration on step 4.
        val jailIds = cpEntries.map { entry ->
            entry.commonJailId
        }
        val jailMap = jailMap(jailIds)

        // 3. Map this to CoPrisonerView entities:
        val relatedPrisonerViews = coPrisonersDao.prisonerViews(cpIds)
        val related = relatedPrisonerViews.map { p ->
            val entry = idToEntryMap[p.id]!!
            val isFirstCurrent = (prisonerId == entry.key.id1)

            val relation = RelationResolver(entry.relationOrdinal).resolve(isFirstCurrent)
            CoPrisonerHeaderPojo(
                p.id,
                p.name,
                jailMap[entry.commonJailId]!!.name,
                entry.commonCellNumber,
                relation.ordinal.toShort()
            )
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

    private fun jailMap(jailIds: List<Int>): HashMap<Int, JailEntity> {
        val jails = jailsDao.get(jailIds)

        val jailMap = hashMapOf<Int, JailEntity>()
        for(j in jails) {
            jailMap[j.id] = j
        }

        return jailMap
    }
}