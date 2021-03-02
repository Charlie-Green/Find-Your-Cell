package by.zenkevich_churun.findcell.result.db.convert

import androidx.room.TypeConverter
import by.zenkevich_churun.findcell.domain.entity.Contact


class ContactTypeRoomConverter {

    @TypeConverter
    fun typeToInt(type: Contact.Type): Int
        = type.ordinal

    @TypeConverter
    fun intToType(int: Int): Contact.Type
        = Contact.Type.values()[int]
}