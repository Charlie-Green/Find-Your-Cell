package by.zenkevich_churun.findcell.domain.simpleentity

import by.zenkevich_churun.findcell.domain.entity.Contact
import by.zenkevich_churun.findcell.domain.entity.Prisoner


class SimplePrisoner(
    override val id: Int,
    override val username: String?,
    override val passwordHash: ByteArray?,
    override val name: String,
    override val info: String,
    override val contacts: List<Contact>
): Prisoner()