package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.prisoner.R


class CellOptionsDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.cell_options_dialog, container, false)


    companion object {

        fun arguments(cell: Cell): Bundle
            = CellOptionsArguments.create(cell)
    }
}