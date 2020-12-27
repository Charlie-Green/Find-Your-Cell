package by.zenkevich_churun.findcell.prisoner.repo.common

import by.zenkevich_churun.findcell.entity.Contact
import by.zenkevich_churun.findcell.entity.Prisoner


class ExtendedPrisoner(
    override val id: Int,
    override val name: String,
    override val contacts: List<Contact>,
    override val info: String,
    override val passwordHash: ByteArray
): Prisoner() {

    override val username: String?
        get() = throw NotImplementedError("Username is not stored for security reasons")
}