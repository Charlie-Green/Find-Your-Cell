package by.zenkevich_churun.findcell.result.db.dao

import androidx.room.*
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerContactEntity
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity


@Dao
interface CoPrisonersDao {

    @Query("delete from CPContacts")
    fun deleteContacts()

    @Query("delete from CoPrisoners")
    fun deleteCoPrisoners()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateCoprisoners(coPrisoners: List<CoPrisonerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateContacts(contacts: List<CoPrisonerContactEntity>)
}