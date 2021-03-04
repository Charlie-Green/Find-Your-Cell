package by.sviazen.server.internal.dao.jail

import by.sviazen.server.internal.entity.table.JailEntity
import by.sviazen.server.internal.entity.view.FullJailView
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


@org.springframework.stereotype.Repository
interface JailsDao: Repository<JailEntity, Int> {

    @Query("select j from JailEntity j")
    fun get(): List<JailEntity>

    @Query("select j from JailEntity j where id in :jailIds")
    fun get(jailIds: List<Int>): List<JailEntity>

    @Query("select j from FullJailView j")
    fun getFull(): List<FullJailView>

    @Query("select seatCount from CellEntity c where jail=:jailId")
    fun getSeatCounts(jailId: Int): List<Short>
}