package by.zenkevich_churun.findcell.prisoner.api.ram.common

import by.zenkevich_churun.findcell.prisoner.api.ram.jail.RamJailEntity


object RamJailsStorage {
    val jails = listOf(
        RamJailEntity(17, "Окрестина ИВС", 24),
        RamJailEntity(11, "Окрестина ЦИП", 40),
        RamJailEntity(24, "Жодино",        210),
        RamJailEntity(16, "Барановичи",    150),
        RamJailEntity(29, "Могилёв",       180)
    )
}