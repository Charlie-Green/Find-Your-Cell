package by.zenkevich_churun.findcell.prisoner.api.ram

import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner


internal class PrisonerRamEntity(
    override val id: Int,
    override val name: String,
    val username: String,
    val passwordHash: ByteArray,
    override val contacts: List<Contact>,
    override val info: String
): Prisoner()