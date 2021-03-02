package by.zenkevich_churun.findcell.server.internal.repo.arest

import by.zenkevich_churun.findcell.domain.entity.Arest
import by.zenkevich_churun.findcell.domain.contract.arest.AddedArestPojo
import by.zenkevich_churun.findcell.domain.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.speriod.SchedulePeriodsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class ArestsRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var arestsDao: ArestsDao

    @Autowired
    private lateinit var scheduleDao: SchedulePeriodsDao


    fun getArests(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<ArestView> {

        validateCredentials(prisonerId, passwordHash)
        val arests = arestsDao.arests(prisonerId)

        return arests.map { arest ->
            val jailIds = arestsDao.jailIds(arest.id)
            ArestView(arest, jailIds)
        }
    }

    fun addArest(
        data: AddedArestPojo,
        passwordHash: ByteArray
    ): CreateOrUpdateArestResponse {

        validateCredentials(data.prisonerId, passwordHash)

        ArestsUtil.validate(data.start, data.end)
        val entity = ArestEntity(
            Arest.INVALID_ID,
            data.prisonerId,
            data.start,
            data.end
        )
        ArestsUtil.normalize(entity)

        val intersectingArests = arestsDao.intersectingArests(
            data.prisonerId,
            entity.start,
            entity.end
        )
        if(!intersectingArests.isEmpty()) {
            val id = intersectingArests[0]
            return CreateOrUpdateArestResponse.ArestsIntersect(id)
        }

        arestsDao.save(entity)
        return CreateOrUpdateArestResponse.Success(entity.id)
    }


    fun deleteArests(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestIds: List<Int> ) {

        validateCredentials(prisonerId, passwordHash)
        scheduleDao.deleteCellEntriesForArests(arestIds)
        scheduleDao.deletePeriodsForArests(arestIds)
        arestsDao.delete(arestIds)
    }
}