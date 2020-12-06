package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.os.Bundle
import androidx.core.os.bundleOf
import by.zenkevich_churun.findcell.core.entity.general.Cell


internal object CellOptionsArguments {
    private const val ARG_JAIL_ID = "jail"
    private const val ARG_CELL_NUMBER = "cell"


    fun create(cell: Cell): Bundle {
        return bundleOf(
            ARG_JAIL_ID to cell.jailId,
            ARG_CELL_NUMBER to cell.number
        )
    }
}