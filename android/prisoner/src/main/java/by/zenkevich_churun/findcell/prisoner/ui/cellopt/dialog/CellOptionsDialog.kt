package by.zenkevich_churun.findcell.prisoner.ui.cellopt.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import by.zenkevich_churun.findcell.core.entity.general.Cell
import by.zenkevich_churun.findcell.prisoner.R
import by.zenkevich_churun.findcell.prisoner.ui.cellopt.vm.CellOptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CellOptionsDialog: DialogFragment() {

    @Inject
    lateinit var vm: CellOptionsViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.cell_options_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields()
    }

    override fun onDismiss(dialog: DialogInterface) {
        vm.notifyUiDismissed()
    }


    private fun initFields() {
        val appContext = requireContext().applicationContext
        vm = CellOptionsViewModel.get(appContext, this)
    }


    companion object {

        fun arguments(cell: Cell): Bundle
            = CellOptionsArguments.create(cell)
    }
}