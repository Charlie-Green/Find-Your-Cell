package by.zenkevich_churun.findcell.server.internal.dao.sched

import by.zenkevich_churun.findcell.server.internal.entity.key.PeriodKey
import by.zenkevich_churun.findcell.server.internal.entity.table.PeriodEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import javax.transaction.Transactional


@org.springframework.stereotype.Repository
interface ScheduleDao: Repository<PeriodEntity, PeriodKey> {

    @Query("select p from PeriodEntity p where arest=:arestId")
    fun periods(arestId: Int): List<PeriodEntity>
    

    @Transactional
    @Modifying
    @Query("delete from PeriodEntity p where arest in :arests")
    fun deleteForArests(arests: List<Int>)
}