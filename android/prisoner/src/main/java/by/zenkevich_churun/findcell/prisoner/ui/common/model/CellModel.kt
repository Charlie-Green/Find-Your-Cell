package by.zenkevich_churun.findcell.prisoner.ui.common.model

import by.zenkevich_churun.findcell.core.entity.general.Cell


class CellModel(
    override val jailName: String,
    override val number: Short,
    override val seats: Short,
    val backColor: Int,
    val numberBackColor: Int,
    val textColor: Int
): Cell() {

    override fun toString(): String
        = "$jailName, $number"
}