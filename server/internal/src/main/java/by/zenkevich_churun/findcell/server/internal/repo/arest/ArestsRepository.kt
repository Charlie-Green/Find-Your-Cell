package by.zenkevich_churun.findcell.server.internal.repo.arest

import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.common.CommonDao
import by.zenkevich_churun.findcell.server.internal.entity.view.ArestView


class ArestsRepository(
    private val dao: ArestsDao,
    private val commonDao: CommonDao ) {

    fun getArests(
        prisonerId: Int,
        passwordHash: ByteArray
    ): List<ArestView> {

        commonDao.validateCredentials(prisonerId, passwordHash)
        val arests = dao.getArests(prisonerId)
        TODO()
    }
}