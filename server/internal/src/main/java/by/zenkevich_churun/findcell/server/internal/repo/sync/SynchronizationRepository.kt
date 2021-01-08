package by.zenkevich_churun.findcell.server.internal.repo.sync

import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.entity.pojo.SynchronizedPojo
import by.zenkevich_churun.findcell.server.internal.dao.coprisoner.CoPrisonersDao
import by.zenkevich_churun.findcell.server.internal.dao.jail.JailsDao
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class SynchronizationRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var jailsDao: JailsDao

    @Autowired
    private lateinit var coPrisonersDao: CoPrisonersDao


    fun suggestedPrisoners(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<Prisoner> {

        // 1. Validate Credentials.
        validateCredentials(prisonerId, passwordHash)

        // 2. Get all Periods for the specified Prisoner:
        val myPeriods = coPrisonersDao.periods(prisonerId)

        // 3. Prepare the excluded arest IDs list:
        val excludedArestIdsSet = hashSetOf<Int>()
        for(period in myPeriods) {
            excludedArestIdsSet.add(period.key!!.arestId)
        }
        val excludedArestIds = excludedArestIdsSet.toList()

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

        // 4. Map Arest IDs to Prisoners:
        return coPrisonersDao.prisonersByArests(othersArestIds.toList())
    }
}