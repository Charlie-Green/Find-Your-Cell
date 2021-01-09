package by.zenkevich_churun.findcell.result.db.entity

import androidx.room.*
import by.zenkevich_churun.findcell.entity.entity.Contact


@Entity(
    tableName = "CPContacts",

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
    override val type: Contact.Type,

    @ColumnInfo(name = "data")
    override val data: String

): Contact() {


    companion object {

        fun from(
            contact: Contact,
            coPrisonerId: Int
        ): CoPrisonerContactEntity {

            return CoPrisonerContactEntity(
                coPrisonerId,
                contact.type,
                contact.data
            )
        }
    }
}