package by.zenkevich_churun.findcell.result.db.convert

import androidx.room.TypeConverter
import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


class CoPrisonerRelationRoomConverter {

    @TypeConverter
    fun relationToInt(relation: CoPrisoner.Relation): Int
        = relation.ordinal

    @TypeConverter
    fun shortToRelation(int: Int): CoPrisoner.Relation
        = CoPrisoner.Relation.values()[int]
}