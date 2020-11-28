package by.zenkevich_churun.findcell.prisoner.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.zenkevich_churun.findcell.prisoner.db.entity.JailEntity


@Dao
interface JailsDao {
    @Query("select * from Jails")
    fun jails(): List<JailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(jails: List<JailEntity>)

    @Query("delete from Jails where id in (:ids)")
    fun delete(ids: List<Int>)
}