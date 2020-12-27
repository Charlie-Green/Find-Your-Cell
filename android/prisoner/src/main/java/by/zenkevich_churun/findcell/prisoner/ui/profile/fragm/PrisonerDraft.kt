package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import by.zenkevich_churun.findcell.entity.Contact
import by.zenkevich_churun.findcell.entity.Prisoner


internal class PrisonerDraft(
    override val id: Int,
    override val name: String,
    override val contacts: List<Contact>,
    override val info: String
): Prisoner() {

    override val username: String?
        get() = throw NotImplementedError("Username is not displayed on Profile screen")

    override val passwordHash: ByteArray?
        get() = throw NotImplementedError("Password has nothing to do with Profile screen")
}