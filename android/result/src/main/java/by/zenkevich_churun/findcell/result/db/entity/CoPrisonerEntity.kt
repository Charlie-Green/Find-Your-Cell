package by.zenkevich_churun.findcell.result.db.entity

import androidx.room.*
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


@Entity
class CoPrisonerEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "info")
    val info: String,

    @ColumnInfo(name = "rel")
    val relation: CoPrisoner.Relation
)