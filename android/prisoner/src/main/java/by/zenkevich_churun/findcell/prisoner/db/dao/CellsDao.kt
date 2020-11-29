package by.zenkevich_churun.findcell.prisoner.db.dao

import androidx.room.*
import by.zenkevich_churun.findcell.prisoner.db.entity.CellEntity


@Dao
interface CellsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(cells: List<CellEntity>)

    @Query("delete from Cells where jail=:jailId and number in (:numbers)")
    fun delete(jailId: Int, numbers: List<Short>)
}