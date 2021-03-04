package by.sviazen.server.internal.dao.cp

import by.sviazen.server.internal.entity.key.CoPrisonerKey
import by.sviazen.server.internal.entity.table.*
import by.sviazen.server.internal.entity.view.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


@org.springframework.stereotype.Repository
interface CoPrisonersDao: Repository<CoPrisonerEntity, CoPrisonerKey> {

    /** @return All [PeriodEntity]s for the specified [PrisonerEntity]. **/
    @Query(
        "select p from PeriodEntity p where arest in(" +
            "select id from ArestEntity a where prisoner=:prisonerId" +
        ")"
    )
    fun periods(prisonerId: Int): List<PeriodEntity>


    @Query("select id from ArestEntity a where prisoner in :prisonerIds")
    fun arestIdsByPrisoners(prisonerIds: List<Int>): List<Int>

    @Query(
        "select p from PrisonerView p where id in(" +
            "select a.prisonerId from ArestEntity a where a.id in :arestIds" +
        ")"
    )
    fun prisonerViewsByArests(arestIds: List<Int>): List<PrisonerView>


    /** @return [List] of IDs of [ArestEntity]s to have at least 1 [PeriodEntity]
      *         intersecting the specified Period (by both dates and [Cell]),
      *         excluding specified IDs. **/
    @Query(
        "select distinct p.key.arestId from PeriodEntity p " +
        "where (p.jailId = :jailId) and (p.cellNumber = :cellNumber) and " +
        "(p.key.arestId not in :excludedArestIds) and (" +
            PERIODS_INTERSECT_CRITERIA +
        ")"
    )
    fun getCoArestIds(
        jailId: Int, cellNumber: Short,
        periodStart: Long, periodEnd: Long,
        excludedArestIds: List<Int>
    ): List<Int>

    @Query(
        "select cp from CoPrisonerEntity cp " +
        "where (cp.key.id1=:id1 and cp.key.id2=:id2) or " +
              "(cp.key.id1=:id2 and cp.key.id2=:id1)"
    )
    fun coPrisoner(
        id1: Int,
        id2: Int
    ): CoPrisonerEntity?

    /** Counts the number of [PeriodEntity]s from the given list of [ArestEntity] IDs
      * and intersecting with the specified [PeriodEntity]. **/
    @Query(
        "select count(p) " +
        "from PeriodEntity p " +
        "where (arest in :arestIds) and " +
               "(jail=:jailId) and (cell=:cellNumber) and (" +
                   PERIODS_INTERSECT_CRITERIA +
               ")"
    )
    fun countIntersections(
        arestIds: List<Int>,
        periodStart: Long, periodEnd: Long,
        jailId: Int, cellNumber: Short
    ): Int


    @Query("select c from CoPrisonerEntity c where p1=:prisonerId or p2=:prisonerId")
    fun coPrisonerEntries(prisonerId: Int): List<CoPrisonerEntity>

    @Query("select p from PrisonerView p where p.id in :prisonerIds")
    fun prisonerViews(prisonerIds: List<Int>): List<PrisonerView>


    fun save(cp: CoPrisonerEntity)

    fun deleteById(key: CoPrisonerKey)


    companion object {
        /** A piece of JPQL code to check if 2 [PeriodEntity]s,
          * one named p and the other specified by 4 query parameters,
          * intersect. **/
        private const val PERIODS_INTERSECT_CRITERIA =                // A intersects B  <=>  either:
            "(p.key.start between :periodStart and :periodEnd) "   +  // - A.start between B.start and B.end
            "or (p.key.end between :periodStart and :periodEnd) "  +  // - A.end   between B.start and B.end
            "or (:periodStart between p.key.start and p.key.end) " +  // - B.start between A.start and A.end
            "or (:periodEnd between p.key.start and p.key.end)"       // - B.end   between A.start and A.end
    }
}