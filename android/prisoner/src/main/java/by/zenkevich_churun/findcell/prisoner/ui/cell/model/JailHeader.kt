package by.zenkevich_churun.findcell.prisoner.ui.cell.model

import by.zenkevich_churun.findcell.core.entity.general.Jail


class JailHeader(
    override val id: Int,
    override val name: String,
    override val cellCount: Short
): Jail() {

    override fun toString(): String
        = name


    companion object {
        fun from(jail: Jail): JailHeader {
            if(jail is JailHeader) {
                return jail
            }
            return JailHeader(jail.id, jail.name, jail.cellCount)
        }
    }
}