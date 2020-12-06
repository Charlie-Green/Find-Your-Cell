package by.zenkevich_churun.findcell.prisoner.ui.cell.model

import by.zenkevich_churun.findcell.core.entity.general.Jail


class CellEditorState(
    val jails: List<Jail>,
    val oldJailIndex: Int,
    val jailIndex: Int,
    val oldCellNumber: Short,
    val cellNumber: Short,
    val isNew: Boolean ) {

    constructor(jails: List<Jail>, jailIndex: Int, cellNumber: Short, isNew: Boolean):
        this(jails, jailIndex, jailIndex, cellNumber, cellNumber, isNew)


    val selectedJail: Jail
        get() = jails[jailIndex]

    val oldSelectedJail: Jail
        get() = jails[oldJailIndex]
}