package by.sviazen.server.internal.entity.view

import by.sviazen.domain.entity.LightArest
import by.sviazen.server.internal.entity.table.ArestEntity
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