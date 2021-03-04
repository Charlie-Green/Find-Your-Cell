package by.sviazen.result.db.convert

import androidx.room.TypeConverter
import by.sviazen.domain.entity.CoPrisoner


class CoPrisonerRelationRoomConverter {

    @TypeConverter
    fun relationToInt(relation: CoPrisoner.Relation): Int
        = relation.ordinal

    @TypeConverter
    fun shortToRelation(int: Int): CoPrisoner.Relation
        = CoPrisoner.Relation.values()[int]
}