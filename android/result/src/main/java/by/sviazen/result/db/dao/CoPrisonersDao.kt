package by.sviazen.result.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import by.sviazen.domain.entity.CoPrisoner
import by.sviazen.result.db.entity.CoPrisonerEntity


@Dao
interface CoPrisonersDao {

    @Query("select * from CoPrisoners where rel in (:allowedRelations)")
    fun coPrisonersLD(
        allowedRelations: List<CoPrisoner.Relation>
    ): LiveData< List<CoPrisonerEntity> >

    @Query("update CoPrisoners set rel=:newRelation where id=:coPrisonerId")
    fun updateRelation(coPrisonerId: Int, newRelation: CoPrisoner.Relation)

    @Query("delete from CoPrisoners")
    fun deleteCoPrisoners()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateCoprisoners(coPrisoners: List<CoPrisonerEntity>)
}