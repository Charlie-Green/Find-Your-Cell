package by.zenkevich_churun.findcell.server.internal.repo.arest

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.common.CommonDao
import by.zenkevich_churun.findcell.server.internal.dao.sched.ScheduleDao
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView


class ArestsRepository(
    private val dao: ArestsDao,
    private val commonDao: CommonDao,
    private val scheduleDao: ScheduleDao ) {

    fun getArests(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<ArestView> {

        commonDao.validateCredentials(prisonerId, passwordHash)
        val arests = dao.arests(prisonerId)

        return arests.map { arest ->
            val jailIds = dao.jailIds(arest.id)
            ArestView(arest, jailIds)
        }
    }

    fun addArest(
        arest: LightArest,
        prisonerId: Int,
        passwordHash: ByteArray
    ): CreateOrUpdateArestResponse {

        commonDao.validateCredentials(prisonerId, passwordHash)

        ArestsUtil.validate(arest)
        val entity = ArestEntity.from(arest, prisonerId)
        ArestsUtil.normalize(entity)

        val intersectingArests = dao.intersectingArests(
            prisonerId,
            entity.start,
            entity.end
        )
        if(!intersectingArests.isEmpty()) {
            val id = intersectingArests[0]
            return CreateOrUpdateArestResponse.ArestsIntersect(id)
        }

        dao.add(entity)
        return CreateOrUpdateArestResponse.Success(entity.id)
    }


    fun deleteArests(
        prisonerId: Int,
        passwordHash: ByteArray,
        arestIds: List<Int> ) {

        commonDao.validateCredentials(prisonerId, passwordHash)
        scheduleDao.deleteForArests(arestIds)
        dao.delete(arestIds)
    }
}