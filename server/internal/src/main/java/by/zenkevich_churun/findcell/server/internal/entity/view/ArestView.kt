package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.LightArest
import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity
import java.util.Calendar


class ArestView(
    val arest: ArestEntity,
    val jailIds: List<Int>
): LightArest() {

    override val id: Int
        get() = arest.id

    override val start: Calendar by lazy {
        Calendar.getInstance().apply {
            timeInMillis = arest.start
        }
    }

    override val end: Calendar by lazy {
        Calendar.getInstance().apply {
            timeInMillis = arest.end
        }
    }

    override val jailsCount: Int
        get() = jailIds.size

    override fun jailIdAt(index: Int): Int
        = jailIds[index]
}