package by.zenkevich_churun.findcell.result.db.entity

import androidx.room.*
import by.zenkevich_churun.findcell.entity.entity.Contact


@Entity(
    primaryKeys = ["prisoner", "type"],

    foreignKeys = [
        ForeignKey(
            entity = CoPrisonerEntity::class,
            parentColumns = ["id"],
            childColumns = ["prisoner"]
        )
    ]
)
class CoPrisonerContactEntity(

    @ColumnInfo(name = "prisoner")
    val coPrisonerId: Int,

    @ColumnInfo(name = "type")
    val type: Contact.Type,

    @ColumnInfo(name = "data")
    val data: String
)