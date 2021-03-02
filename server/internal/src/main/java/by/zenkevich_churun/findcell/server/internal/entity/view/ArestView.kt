package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.domain.entity.LightArest
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import java.util.Calendar


class ArestView(
    val arest: ArestEntity,
    val jailIds: List<Int>
): LightArest() {

    override val id         by arest::id
    override val start      by arest::start
    override val end        by arest::end
    override val jailsCount by jailIds::size

    override fun jailIdAt(index: Int): Int
        = jailIds[index]
}