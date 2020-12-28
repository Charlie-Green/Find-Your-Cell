package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.zenkevich_churun.findcell.entity.entity.Cell


internal class CellOptionsArguments private constructor(
    val jailId: Int,
    val cellNumber: Short ) {


    companion object {
        private const val ARG_JAIL_ID = "jail"
        private const val ARG_CELL_NUMBER = "cell"

        fun from(fragm: Fragment): CellOptionsArguments {
            val args = fragm.requireArguments()
            return CellOptionsArguments(
                args.getInt(ARG_JAIL_ID),
                args.getShort(ARG_CELL_NUMBER)
            )
        }

        fun create(cell: Cell): Bundle {
            return bundleOf(
                ARG_JAIL_ID to cell.jailId,
                ARG_CELL_NUMBER to cell.number
            )
        }
    }
}