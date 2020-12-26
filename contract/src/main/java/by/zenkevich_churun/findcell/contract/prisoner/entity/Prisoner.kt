package by.zenkevich_churun.findcell.contract.prisoner.entity


class Prisoner(
    val id: Int,
    val username: String?,
    val passwordHash: ByteArray?,
    val name: String,
    val info: String,
    val contacts: List<Contact>
)