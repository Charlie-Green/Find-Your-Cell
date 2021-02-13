package by.zenkevich_churun.findcell.server.internal.dao.jail

import by.zenkevich_churun.findcell.server.internal.entity.table.JailEntity
import by.zenkevich_churun.findcell.server.internal.entity.view.FullJailView
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository


@org.springframework.stereotype.Repository
interface JailsDao: Repository<JailEntity, Int> {

    @Query("select j from JailEntity j")
    fun get(): List<JailEntity>

    @Query("select j from FullJailView j")
    fun getFull(): List<FullJailView>

    @Query("select seatCount from CellEntity c where jail=:jailId")
    fun getSeatCounts(jailId: Int): List<Short>

    @Query("select name from JailEntity j where id=:id")
    fun nameOf(id: Int): String
}