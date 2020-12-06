package by.zenkevich_churun.findcell.prisoner.ui.cell.vm

import android.content.Context
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil
import by.zenkevich_churun.findcell.prisoner.R


internal class CellVMMapping(
    private val appContext: Context ) {

    val needInternetMessage: String
        get() = AndroidUtil.mergeStrings(
            appContext,
            R.string.cell_commonpart_msg,
            R.string.cell_need_internet_msg
        )

    val getJailsErrorMessage: String
        get() = AndroidUtil.mergeStrings(
            appContext,
            R.string.cell_commonpart_msg,
            R.string.cell_error_msg
        )

    val addCellFailedMessage: String
        get() = appContext.getString(R.string.add_cell_failed_msg)

    val updateCellFailedMessage: String
        get() = appContext.getString(R.string.update_cell_failed_msg)
}