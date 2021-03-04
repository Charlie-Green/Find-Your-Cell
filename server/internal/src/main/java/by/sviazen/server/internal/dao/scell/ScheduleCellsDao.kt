package by.sviazen.server.internal.dao.scell

import by.sviazen.server.internal.entity.key.ScheduleCellEntryKey
import by.sviazen.server.internal.entity.table.ScheduleCellEntryEntity
import by.sviazen.server.internal.entity.view.FullCellView
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


interface ScheduleCellsDao: Repository<ScheduleCellEntryEntity, ScheduleCellEntryKey> {

    @Query("select x from ScheduleCellEntryEntity x where arest=:arestId")
    fun get(arestId: Int): List<ScheduleCellEntryEntity>

    @Query("select c from FullCellView c where c.jail.id=:jailId and c.number=:cellNumber")
    fun getFull(jailId: Int, cellNumber: Short): FullCellView

    fun save(entity: ScheduleCellEntryEntity)

    fun deleteById(id: ScheduleCellEntryKey)
}