package by.zenkevich_churun.findcell.prisoner.ui.cell.dialog

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment


internal class CellDialogArguments private constructor(
    val jailId: Int,
    val cellNumber: Short ) {

    companion object {
        private const val ATTR_JAIL_ID = "jail"
        private const val ATTR_CELL_NUMBER = "cell"


        fun create(jailId: Int, cellNumber: Short): Bundle {
            return bundleOf(
                ATTR_JAIL_ID to jailId,
                ATTR_CELL_NUMBER to cellNumber
            )
        }

        fun from(fragment: Fragment): CellDialogArguments {
            val args = fragment.requireArguments()
            return CellDialogArguments(
                args.getInt(ATTR_JAIL_ID),
                args.getShort(ATTR_CELL_NUMBER)
            )
        }
    }
}