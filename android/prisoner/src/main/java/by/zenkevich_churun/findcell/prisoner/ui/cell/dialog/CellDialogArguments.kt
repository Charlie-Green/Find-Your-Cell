package by.zenkevich_churun.findcell.prisoner.ui.cell.dialog

import android.os.Bundle
import androidx.core.os.bundleOf


internal object CellDialogArguments {
    private const val ATTR_JAIL_ID = "jail"
    private const val ATTR_CELL_NUMBER = "cell"


    fun create(jailId: Int, cellNumber: Short): Bundle {
        return bundleOf(
            ATTR_JAIL_ID to jailId,
            ATTR_CELL_NUMBER to cellNumber
        )
    }


    fun jailId(bundle: Bundle): Int {
        return bundle.getInt(ATTR_JAIL_ID)
    }

    fun cellNumber(bundle: Bundle): Short {
        return bundle.getShort(ATTR_CELL_NUMBER)
    }
}