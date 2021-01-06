package by.zenkevich_churun.findcell.server.internal.repo.sched

import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.scell.ScheduleCellsDao
import by.zenkevich_churun.findcell.server.internal.dao.speriod.SchedulePeriodsDao
import by.zenkevich_churun.findcell.server.internal.entity.view.ScheduleView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class ScheduleRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var periodsDao: SchedulePeriodsDao

    @Autowired
    private lateinit var cellsDao: ScheduleCellsDao

    @Autowired
    private lateinit var arestsDao: ArestsDao


    fun get(
        arestId: Int,
        passwordHash: ByteArray
    ): ScheduleView {

        val prisonerId = arestsDao.prisonerId(arestId)
        validateCredentials(prisonerId, passwordHash)

        return ScheduleView(
            arestsDao.findById(arestId).get(),
            cellsDao.get(arestId),
            periodsDao.get(arestId)
        )
    }
}