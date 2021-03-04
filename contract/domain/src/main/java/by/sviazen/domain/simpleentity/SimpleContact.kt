package by.sviazen.domain.simpleentity

import by.sviazen.domain.entity.Contact


class SimpleContact(
    override val type: Type,
    override val data: String
): Contact()