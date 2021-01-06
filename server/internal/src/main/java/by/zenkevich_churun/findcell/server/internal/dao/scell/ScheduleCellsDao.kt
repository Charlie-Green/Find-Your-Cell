package by.zenkevich_churun.findcell.server.internal.dao.scell

import by.zenkevich_churun.findcell.server.internal.entity.key.ScheduleCellEntryKey
import by.zenkevich_churun.findcell.server.internal.entity.table.ScheduleCellEntryEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


interface ScheduleCellsDao: Repository<ScheduleCellEntryEntity, ScheduleCellEntryKey> {

    @Query("select x from ScheduleCellEntryEntity x where arest=:arestId")
    fun get(arestId: Int): List<ScheduleCellEntryEntity>

    fun save(entity: ScheduleCellEntryEntity)

    fun deleteById(id: ScheduleCellEntryKey)
}