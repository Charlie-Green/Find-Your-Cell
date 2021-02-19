package by.zenkevich_churun.findcell.server.internal.repo.sched

import by.zenkevich_churun.findcell.entity.entity.SchedulePeriod
import by.zenkevich_churun.findcell.server.internal.dao.arest.ArestsDao
import by.zenkevich_churun.findcell.server.internal.dao.scell.ScheduleCellsDao
import by.zenkevich_churun.findcell.server.internal.dao.speriod.SchedulePeriodsDao
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
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

        validateCredentialsByArestId(arestId, passwordHash)

        return ScheduleView(
            arestsDao.findById(arestId).get(),
            cellsDao.get(arestId),
            periodsDao.get(arestId)
        )
    }


    fun save(
        arestId: Int,
        periods: List<SchedulePeriod>,
        passwordHash: ByteArray ) {

        validateCredentialsByArestId(arestId, passwordHash)

        val cells = cellsDao.get(arestId)
        val entities = periods.map { period ->
            PeriodEntity.from(arestId, period, cells)
        }

        periodsDao.apply {
            deletePeriodsForArests( listOf(arestId) )
            saveAll(entities)
        }
    }


    private fun validateCredentialsByArestId(arestId: Int, passwordHash: ByteArray) {
        val prisonerId = arestsDao.prisonerId(arestId)
        validateCredentials(prisonerId, passwordHash)
    }
}