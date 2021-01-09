package by.zenkevich_churun.findcell.result.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerContactEntity
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity


@Dao
interface CoPrisonersDao {

    @Query("select * from CoPrisoners where rel in (:allowedRelations)")
    fun coPrisonersLD(
        allowedRelations: List<CoPrisoner.Relation>
    ): LiveData< List<CoPrisonerEntity> >

    @Query("select * from CPContacts where prisoner=:coPrisonerId")
    fun contacts(coPrisonerId: Int): List<CoPrisonerContactEntity>


    @Query("delete from CPContacts")
    fun deleteContacts()

    @Query("delete from CoPrisoners")
    fun deleteCoPrisoners()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateCoprisoners(coPrisoners: List<CoPrisonerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateContacts(contacts: List<CoPrisonerContactEntity>)
}