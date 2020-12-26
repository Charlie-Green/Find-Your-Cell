package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.server.internal.entity.table.ArestEntity


class ArestView(
    val arest: ArestEntity,
    val jailIds: List<Int>
)