package by.zenkevich_churun.findcell.result.db.entity

import androidx.room.*
import by.zenkevich_churun.findcell.domain.contract.cp.CoPrisonerHeaderPojo
import by.zenkevich_churun.findcell.domain.entity.CoPrisoner


@Entity(tableName = "CoPrisoners")
class CoPrisonerEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "rel")
    override val relation: CoPrisoner.Relation,

    @ColumnInfo(name = "jail")
    override val commonJailName: String,

    @ColumnInfo(name = "cell")
    override val commonCellNumber: Short

): CoPrisoner() {


    companion object {

        fun from(coPrisoner: CoPrisonerHeaderPojo): CoPrisonerEntity {
            return CoPrisonerEntity(
                coPrisoner.id,
                coPrisoner.name,
                CoPrisoner.Relation.values()[coPrisoner.relationOrdinal.toInt()],
                coPrisoner.commonJailName,
                coPrisoner.commonCellNumber
            )
        }
    }
}