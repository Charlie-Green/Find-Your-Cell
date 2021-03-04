package by.sviazen.domain.simpleentity

import by.sviazen.domain.entity.Jail


class SimpleJail(
    override val id: Int,
    override val name: String,
    override val cellCount: Short
): Jail()