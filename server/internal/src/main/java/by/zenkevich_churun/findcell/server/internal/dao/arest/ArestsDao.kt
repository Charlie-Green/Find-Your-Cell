package by.zenkevich_churun.findcell.server.internal.dao.arest

import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.util.Optional
import javax.transaction.Transactional


@org.springframework.stereotype.Repository
interface ArestsDao: Repository<ArestEntity, Int> {

    fun findById(id: Int): Optional<ArestEntity>

    @Query("select a from ArestEntity a where prisoner=:prisonerId")
    fun arests(prisonerId: Int): List<ArestEntity>

    @Query(
        "select id from ArestEntity a " +
        "where (a.prisonerId = :prisonerId) and (" +      // A intersects B  <=>  either:
            "(a.start > :start and a.start < :end) " +    // - A.start between B.start and B.end
            "or (:start > a.start and :start < a.end) " + // - B.start between A.start and A.end
            "or (a.end > :start and a.end < :end) " +     // - A.end   between B.start and B.end
            "or (:end > a.start and :end < a.end) " +     // - B.end   between A.start and A.end
            "or (a.start = :start and a.end = :end)" +    // - A equals B
        ")"
    )
    fun intersectingArests(
        prisonerId: Int,
        start: Long,
        end: Long
    ): List<Int>

    @Query("select prisonerId from ArestEntity a where id=:arestId")
    fun prisonerId(arestId: Int): Int

    @Query("select distinct jailId from PeriodEntity p where arest=:arestId")
    fun jailIds(arestId: Int): List<Int>

    fun save(arest: ArestEntity)

    @Transactional
    @Modifying
    @Query("delete from ArestEntity where id in :ids")
    fun delete(ids: List<Int>)
}