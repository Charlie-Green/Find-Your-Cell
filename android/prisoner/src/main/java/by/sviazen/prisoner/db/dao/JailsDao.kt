package by.sviazen.prisoner.db.dao

import androidx.room.*
import by.sviazen.prisoner.db.entity.CellEntity
import by.sviazen.prisoner.db.entity.JailEntity


@Dao
interface JailsDao {
    @Query("select * from Jails")
    fun jails(): List<JailEntity>

    @Query("select name from Jails where id=:id")
    fun jailName(id: Int): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(jails: List<JailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateCells(cells: List<CellEntity>)


    @Query("delete from Jails where id in (:ids)")
    fun delete(ids: List<Int>)

    @Query("delete from Cells")
    fun deleteCells()
}