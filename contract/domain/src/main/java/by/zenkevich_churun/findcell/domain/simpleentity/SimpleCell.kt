package by.zenkevich_churun.findcell.domain.simpleentity

import by.zenkevich_churun.findcell.domain.entity.Cell


class SimpleCell(
    override val jailId: Int,
    override val jailName: String,
    override val number: Short,
    override val seats: Short
): Cell()