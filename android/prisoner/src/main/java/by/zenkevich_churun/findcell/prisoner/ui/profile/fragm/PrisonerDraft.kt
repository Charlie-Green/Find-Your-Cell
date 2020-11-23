package by.zenkevich_churun.findcell.prisoner.ui.profile.fragm

import by.zenkevich_churun.findcell.core.entity.Contact
import by.zenkevich_churun.findcell.core.entity.Prisoner


internal class PrisonerDraft(
    override val id: Int,
    override val name: String,
    override val contacts: List<Contact>,
    override val info: String
): Prisoner()