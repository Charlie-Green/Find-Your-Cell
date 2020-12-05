package by.zenkevich_churun.findcell.prisoner.ui.cell.model

import by.zenkevich_churun.findcell.core.entity.general.Jail


class CellEditorState(
    val jails: List<Jail>,
    val jailIndex: Int,
    val cellNumber: Short,
    val isNew: Boolean ) {

    val selectedJail: Jail
        get() = jails[jailIndex]
}