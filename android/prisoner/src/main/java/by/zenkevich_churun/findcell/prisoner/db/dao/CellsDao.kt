package by.zenkevich_churun.findcell.prisoner.db.dao

import androidx.room.*
import by.zenkevich_churun.findcell.domain.simpleentity.SimpleCell
import by.zenkevich_churun.findcell.prisoner.db.entity.CellEntity


@Dao
interface CellsDao {
    @Query("select :jailId as jailId, " +
                  "J.name as jailName, " +
                  ":cellNumber as number, " +
                  "C.seats as seats " +
           "from Jails J inner join Cells C on J.id=C.jail " +
           "where J.id=:jailId and C.number=:cellNumber" )
    fun get(jailId: Int, cellNumber: Short): SimpleCell?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(cells: List<CellEntity>)

    @Query("delete from Cells where jail=:jailId and number in (:numbers)")
    fun delete(jailId: Int, numbers: List<Short>)
}