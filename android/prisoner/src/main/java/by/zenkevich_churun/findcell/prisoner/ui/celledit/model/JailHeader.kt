package by.zenkevich_churun.findcell.prisoner.ui.celledit.model

import by.zenkevich_churun.findcell.entity.Jail


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