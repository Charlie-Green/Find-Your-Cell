package by.zenkevich_churun.findcell.prisoner.ui.celledit.model

import by.zenkevich_churun.findcell.core.entity.general.Jail


class CellEditorState(
    val jails: List<Jail>,
    val oldJailIndex: Int,
    val jailIndex: Int,
    val oldCellNumber: Short,
    val cellNumber: Short ) {

    constructor(jails: List<Jail>, jailIndex: Int, cellNumber: Short):
        this(jails, jailIndex, jailIndex, cellNumber, cellNumber)


    val selectedJail: Jail
        get() = jails[jailIndex]

    val oldSelectedJail: Jail
        get() = jails[oldJailIndex]

    val isNew: Boolean
        get() = (oldCellNumber < 0)
}