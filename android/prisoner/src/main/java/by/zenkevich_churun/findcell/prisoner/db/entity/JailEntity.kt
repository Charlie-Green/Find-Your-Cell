package by.zenkevich_churun.findcell.prisoner.db.entity

import androidx.room.*
import by.zenkevich_churun.findcell.core.entity.general.Jail


@Entity(tableName = "Jails")
class JailEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    override val id: Int,

    @ColumnInfo(name = "name")
    override val name: String
): Jail()