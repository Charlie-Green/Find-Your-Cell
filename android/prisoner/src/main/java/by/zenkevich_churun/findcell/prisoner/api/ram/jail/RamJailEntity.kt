package by.zenkevich_churun.findcell.prisoner.api.ram.jail

import by.zenkevich_churun.findcell.core.entity.general.Jail


class RamJailEntity(
    override val id: Int,
    override val name: String,
    override val cellCount: Short
): Jail() {

    fun copy(): RamJailEntity {
        return RamJailEntity(id, name, cellCount)
    }
}