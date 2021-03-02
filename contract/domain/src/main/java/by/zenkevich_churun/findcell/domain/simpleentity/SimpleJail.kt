package by.zenkevich_churun.findcell.domain.simpleentity

import by.zenkevich_churun.findcell.domain.entity.Jail


class SimpleJail(
    override val id: Int,
    override val name: String,
    override val cellCount: Short
): Jail()