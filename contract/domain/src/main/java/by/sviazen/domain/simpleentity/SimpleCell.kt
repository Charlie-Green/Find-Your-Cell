package by.sviazen.domain.simpleentity

import by.sviazen.domain.entity.Cell


class SimpleCell(
    override val jailId: Int,
    override val jailName: String,
    override val number: Short,
    override val seats: Short
): Cell()