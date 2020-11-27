package by.zenkevich_churun.findcell.prisoner.api.ram

import by.zenkevich_churun.findcell.core.entity.general.Contact
import by.zenkevich_churun.findcell.core.entity.general.Prisoner


internal object RamUserStorage {
    val prisoners = mutableListOf(
        PrisonerRamEntity(
            Prisoner.INVALID_ID + 1,
            "Charlie",
            "charlz123",
            "pass".toByteArray(Charsets.UTF_16),
            listOf(
                Contact.Phone("+1 23 456789"),
                Contact.Telegram("@my_telegram")
            ),
            "This is a fake RAM user."
        )
    )


    fun validate(prisonerId: Int, passwordHash: ByteArray): Boolean {
        val existingPrisoner = synchronized(RamUserStorage) {
            prisoners.find { prisoner ->
                prisoner.id == prisonerId &&
                prisoner.passwordHash.contentEquals(passwordHash)
            }
        }
        return existingPrisoner != null
    }
}