package by.zenkevich_churun.findcell.domain.simpleentity

import by.zenkevich_churun.findcell.domain.entity.Contact


class SimpleContact(
    override val type: Type,
    override val data: String
): Contact()