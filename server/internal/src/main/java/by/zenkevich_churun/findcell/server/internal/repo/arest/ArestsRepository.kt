package by.zenkevich_churun.findcell.server.internal.repo.arest

import by.zenkevich_churun.findcell.entity.entity.LightArest
import by.zenkevich_churun.findcell.entity.response.CreateOrUpdateArestResponse
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.common.CommonDao
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView


class ArestsRepository(
    private val dao: ArestsDao,
    private val commonDao: CommonDao ) {

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

        val intersectingArests = dao.arests(
            prisonerId,
            arest.start.timeInMillis,
            arest.end.timeInMillis
        )
        if(!intersectingArests.isEmpty()) {
            val id = intersectingArests[0].id
            return CreateOrUpdateArestResponse.ArestsIntersect(id)
        }

        val entity = ArestEntity.from(arest, prisonerId)
        dao.add(entity)
        println("Assigned id ${entity.id}")
        return CreateOrUpdateArestResponse.ArestsIntersect(entity.id)
    }
}