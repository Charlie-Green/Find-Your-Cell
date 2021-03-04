package by.sviazen.prisoner.db.entity

import androidx.room.*
import by.sviazen.domain.entity.Jail


@Entity(tableName = "Jails")
class JailEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    override val id: Int,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "cells")
    override val cellCount: Short
): Jail() {

    companion object {

        fun from(jail: Jail): JailEntity {
            if(jail is JailEntity) {
                return jail
            }
            return JailEntity(jail.id, jail.name, jail.cellCount)
        }
    }
}