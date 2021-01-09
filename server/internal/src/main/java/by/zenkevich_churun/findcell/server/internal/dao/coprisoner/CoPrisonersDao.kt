package by.zenkevich_churun.findcell.server.internal.dao.coprisoner

import by.zenkevich_churun.findcell.server.internal.entity.key.PeriodKey
import by.zenkevich_churun.findcell.server.internal.entity.table.*
import by.zenkevich_churun.findcell.server.internal.entity.view.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


@org.springframework.stereotype.Repository
interface CoPrisonersDao: Repository<PeriodEntity, PeriodKey> {

    /** @return All [PeriodEntity]s for the specified [PrisonerEntity]. **/
    @Query(
        "select p from PeriodEntity p where arest in(" +
            "select id from ArestEntity a where prisoner=:prisonerId" +
        ")"
    )
    fun periods(prisonerId: Int): List<PeriodEntity>


    /** @return [List] of IDs of [ArestEntity]s to have at least 1 [PeriodEntity]
      *         intersecting the specified Period (by both dates and [Cell]),
      *         excluding specified IDs. **/
    @Query(
        "select distinct p.key.arestId from PeriodEntity p " +
        "where (p.jailId = :jailId) and (p.cellNumber = :cellNumber) and " +
        "(p.key.arestId not in :excludedArestIds) and (" +           // A intersects B  <=>  either:
            "(p.key.start between :periodStart and :periodEnd) " +   // - A.start between B.start and B.end
            "or (p.key.end between :periodStart and :periodEnd) " +  // - A.end   between B.start and B.end
            "or (:periodStart between p.key.start and p.key.end) " + // - B.start between A.start and A.end
            "or (:periodEnd between p.key.start and p.key.end)" +    // - B.end   between A.start and A.end
        ")"
    )
    fun getCoArestIds(
        jailId: Int,
        cellNumber: Short,
        periodStart: Long,
        periodEnd: Long,
        excludedArestIds: List<Int>
    ): List<Int>

    @Query("select c from CoPrisonerEntity where p1=:prisonerId or p2=:prisonerId")
    fun coPrisonerEntries(prisonerId: Int): List<CoPrisonerEntity>

    // @Query("select c from CoPrisonerView1 where c.id2=:id2")
    // fun coPrisonersById2(id2: Int): List<CoPrisonerView2>

    @Query(
        "select p from SuggestedCoPrisonerView p where id in (" +
            "select a.prisonerId from ArestEntity a where a.id in :arestIds" +
        ")"
    )
    fun coPrisonersByArests(arestIds: List<Int>): List<SuggestedCoPrisonerView>

    @Query("select p from PrisonerView p where p.key.prisonerId in :prisonerIds")
    fun prisonerViews(prisonerIds: List<Int>): List<PrisonerView>
}