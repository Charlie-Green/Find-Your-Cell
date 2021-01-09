package by.zenkevich_churun.findcell.server.internal.repo.sync

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import by.zenkevich_churun.findcell.server.internal.dao.coprisoner.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao
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
    ): List<CoPrisoner> {

        // 1. Validate Credentials.
        validateCredentials(prisonerId, passwordHash)

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

        // 4. Map Arest IDs to Prisoners:
        val suggested: List<CoPrisoner> =
            coPrisonersDao.coPrisonersByArests(othersArestIds.toList())

        // 5. Now add CoPrisoners with other Relations:
        val related = coPrisonersDao.coPrisoners(prisonerId)

        // 6. Merge the two:
        return suggested
            .toMutableList()
            .apply { TODO() /* addAll(related) */ }
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