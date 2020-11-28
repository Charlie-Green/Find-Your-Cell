package by.zenkevich_churun.findcell.prisoner.db.view

import by.zenkevich_churun.findcell.core.entity.general.Cell


class CompositeCellEntity(
    val jailId: Int,
    override val jailName: String,
    override val number: Short,
    override val seats: Short
): Cell()