package by.zenkevich_churun.findcell.server.internal.entity.view

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner
import by.zenkevich_churun.findcell.server.internal.entity.table.PrisonerEntity


/** Combines data from Prisoners and Contacts table to implement [Prisoner] the abstract class. **/
class PrisonerView(
    val prisonerEntity: PrisonerEntity,
    override val contacts: List<Contact>
): Prisoner() {

    override val id: Int
        get() = prisonerEntity.id

    override val username: String?
        get() = prisonerEntity.username

    override val passwordHash: ByteArray?
        get() = prisonerEntity.passwordHash

    override val name: String
        get() = prisonerEntity.name

    override val info: String
        get() = prisonerEntity.info
}