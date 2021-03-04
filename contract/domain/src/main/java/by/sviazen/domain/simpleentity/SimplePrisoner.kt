package by.sviazen.domain.simpleentity

import by.sviazen.domain.entity.Contact
import by.sviazen.domain.entity.Prisoner


class SimplePrisoner(
    override val id: Int,
    override val username: String?,
    override val passwordHash: ByteArray?,
    override val name: String,
    override val info: String,
    override val contacts: List<Contact>
): Prisoner()