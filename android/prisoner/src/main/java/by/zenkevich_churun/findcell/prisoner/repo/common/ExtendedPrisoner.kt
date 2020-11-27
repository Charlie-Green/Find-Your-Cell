package by.zenkevich_churun.findcell.prisoner.repo.common

import by.zenkevich_churun.findcell.core.entity.general.Contact
import by.zenkevich_churun.findcell.core.entity.general.Prisoner


class ExtendedPrisoner(
    override val id: Int,
    override val name: String,
    override val contacts: List<Contact>,
    override val info: String,
    val passwordHash: ByteArray
): Prisoner()