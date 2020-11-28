package by.zenkevich_churun.findcell.prisoner.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.zenkevich_churun.findcell.prisoner.db.entity.CellEntity


@Dao
interface CellsDao {
    @Query("select max(number) from Cells where jail=:jailId")
    fun maxNumber(jailId: Int): Short

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(cells: List<CellEntity>)

    @Query("delete from Cells where jail=:jailId and number in (:numbers)")
    fun delete(jailId: Int, numbers: List<Short>)
}