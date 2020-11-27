package by.zenkevich_churun.findcell.prisoner.api.ram

import by.zenkevich_churun.findcell.core.entity.general.Cell


internal class RamCell(
    override val jailName: String,
    override val number: Short,
    override val seats: Short
): Cell()