package by.zenkevich_churun.findcell.server.internal.dao.speriod

import by.zenkevich_churun.findcell.server.internal.entity.key.PeriodKey
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import javax.transaction.Transactional


@org.springframework.stereotype.Repository
interface SchedulePeriodsDao: Repository<PeriodEntity, PeriodKey> {

    @Query("select p from PeriodEntity p where arest=:arestId")
    fun get(arestId: Int): List<PeriodEntity>


    @Transactional
    @Modifying
    @Query("delete from PeriodEntity p where arest in :arests")
    fun deleteForArests(arests: List<Int>)

    @Transactional
    @Modifying
    @Query(
        "delete from PeriodEntity p " +
        "where arest=:arestId and jail=:jailId and cell=:cellNumber"
    )
    fun deleteForCell(arestId: Int, jailId: Int, cellNumber: Short)
}