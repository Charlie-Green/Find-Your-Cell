package by.zenkevich_churun.findcell.prisoner.api.ram.jail

import by.zenkevich_churun.findcell.core.entity.general.Cell


internal class RamCellEntity(
    override val jailName: String,
    override val number: Short,
    override val seats: Short
): Cell()