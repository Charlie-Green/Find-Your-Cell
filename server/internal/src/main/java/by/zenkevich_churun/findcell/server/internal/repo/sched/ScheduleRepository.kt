package by.zenkevich_churun.findcell.server.internal.repo.sched

import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.sched.ScheduleDao
import by.zenkevich_churun.findcell.server.internal.entity.view.ScheduleView
import by.zenkevich_churun.findcell.server.internal.repo.common.SviazenRepositiory
import org.springframework.beans.factory.annotation.Autowired


class ScheduleRepository: SviazenRepositiory() {

    @Autowired
    private lateinit var scheduleDao: ScheduleDao

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
            scheduleDao.cells(arestId),
            scheduleDao.periods(arestId)
        )
    }
}