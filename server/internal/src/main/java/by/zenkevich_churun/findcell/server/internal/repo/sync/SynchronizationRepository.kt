package by.zenkevich_churun.findcell.server.internal.repo.sync

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import by.zenkevich_churun.findcell.server.internal.dao.coprisoner.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.CoPrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.CoPrisonerView
import by.zenkevich_churun.findcell.server.internal.entity.view.SynchronizedDataView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat


class SynchronizationRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var jailsDao: JailsDao

    @Autowired
    private lateinit var coPrisonersDao: CoPrisonersDao


    fun coPrisoners(
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

        // 2. Get all Periods for the specified Prisoner:
        val myPeriods = coPrisonersDao.periods(prisonerId)

        val charlieDebugDateFormat = SimpleDateFormat("dd.MM")
        charlieDebugList("myPeriods", myPeriods) { p ->
            val start = charlieDebugDateFormat.format( java.util.Calendar.getInstance().apply { timeInMillis = p.key!!.start }.time )
            val end   = charlieDebugDateFormat.format( java.util.Calendar.getInstance().apply { timeInMillis = p.key!!.end   }.time )
            "$start - $end"
        }

        // 3. Prepare the excluded arest IDs list:
        val excludedArestIdsSet = hashSetOf<Int>()
        for(period in myPeriods) {
            excludedArestIdsSet.add(period.key!!.arestId)
        }
        val excludedArestIds = excludedArestIdsSet.toList()

        charlieDebugList("excludedArestIds", excludedArestIds)


        // 3. Get Arest IDs to find potential CoPrisoners:
        val othersArestIds = hashSetOf<Int>()
        for(period in myPeriods) {
            val ids = coPrisonersDao.getCoArestIds(
                period.jailId,
                period.cellNumber,
                period.key!!.start,
                period.key!!.end,
                excludedArestIds
            )

            othersArestIds.addAll(ids)
        }

        charlieDebugList("othersArestIds", othersArestIds.toList())

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
            val id =
                if(entry.key.id1 == prisonerId) entry.key.id2
                else entry.key.id1

            idToEntryMap[id] = entry

            id
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


    private fun <T> charlieDebugList(
        label: String,
        list: List<T>,
        toString: (T) -> String = { t -> t.toString() } ) {

        print("$label: [")
        for(item in list) {
            print(" ${toString(item)}")
        }
        println(" ]")
    }
}