package by.zenkevich_churun.findcell.prisoner.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "Cells",
    primaryKeys = ["jail", "number"],
    foreignKeys = [
        ForeignKey(
            entity = JailEntity::class,
            parentColumns = ["id"],
            childColumns = ["jail"]
        )
    ]
)
class CellEntity(
    @ColumnInfo(name = "jail")
    val jailId: Int,

    @ColumnInfo(name = "number")
    val number: Short,

    @ColumnInfo(name = "seats")
    val seats: Short
)