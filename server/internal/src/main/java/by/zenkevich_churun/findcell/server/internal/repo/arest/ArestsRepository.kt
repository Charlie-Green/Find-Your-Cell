package by.zenkevich_churun.findcell.server.internal.repo.arest

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.prisoner.PrisonerDao
import by.zenkevich_churun.findcell.server.internal.dao.sched.ScheduleDao
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class ArestsRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var arestsDao: ArestsDao

    @Autowired
    private lateinit var scheduleDao: ScheduleDao


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
        arest: LightArest,
        prisonerId: Int,
        passwordHash: ByteArray
    ): CreateOrUpdateArestResponse {

        validateCredentials(prisonerId, passwordHash)

        ArestsUtil.validate(arest)
        val entity = ArestEntity.from(arest, prisonerId)
        ArestsUtil.normalize(entity)

        val intersectingArests = arestsDao.intersectingArests(
            prisonerId,
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
        scheduleDao.deleteForArests(arestIds)
        arestsDao.delete(arestIds)
    }
}