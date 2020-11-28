package by.zenkevich_churun.findcell.prisoner.ui.cell.model

import by.zenkevich_churun.findcell.core.entity.general.Jail


class JailHeader(
    override val id: Int,
    override val name: String
): Jail() {

    override fun toString(): String
        = name
}