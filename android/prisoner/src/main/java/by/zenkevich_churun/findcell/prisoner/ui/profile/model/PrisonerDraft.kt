package by.zenkevich_churun.findcell.prisoner.ui.profile.model

import by.zenkevich_churun.findcell.entity.Contact
import by.zenkevich_churun.findcell.entity.Prisoner


internal class PrisonerDraft(
    override val id: Int,
    override val passwordHash: ByteArray,
    override val name: String,
    override val contacts: List<Contact>,
    override val info: String
): Prisoner() {

    // Username is not needed to update profile.
    override val username: String?
        get() = null
}