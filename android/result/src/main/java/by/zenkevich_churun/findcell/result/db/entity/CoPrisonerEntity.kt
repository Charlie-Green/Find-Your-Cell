package by.zenkevich_churun.findcell.result.db.entity

import androidx.room.*
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


@Entity(tableName = "CoPrisoners")
class CoPrisonerEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "info")
    val info: String,

    @ColumnInfo(name = "rel")
    val relation: CoPrisoner.Relation,

    @ColumnInfo(name = "jail")
    val commonJailName: String,

    @ColumnInfo(name = "cell")
    val commonCellNumber: Short ) {


    companion object {

        fun from(coPrisoner: CoPrisoner): CoPrisonerEntity {
            return CoPrisonerEntity(
                coPrisoner.id,
                coPrisoner.name,
                coPrisoner.info,
                coPrisoner.relation,
                coPrisoner.commonJailName,
                coPrisoner.commonCellNumber
            )
        }
    }
}