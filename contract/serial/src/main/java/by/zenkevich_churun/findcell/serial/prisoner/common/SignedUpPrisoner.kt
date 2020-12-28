package by.zenkevich_churun.findcell.serial.prisoner.common

import by.zenkevich_churun.findcell.entity.entity.Contact
import by.zenkevich_churun.findcell.entity.entity.Prisoner


/** Implementation of [Prisoner] used internally
  * by the Sign Up operation implementation. **/
internal class SignedUpPrisoner(
    override val id: Int,
    override val username: String,
    override val passwordHash: ByteArray,
    override val name: String
): Prisoner() {

    /** No info yet, because the user has just signed up. **/
    override val info: String
        get() = ""

    /** No [Contact]s yet because the user has just signed up. **/
    override val contacts: List<Contact>
        get() = listOf()
}